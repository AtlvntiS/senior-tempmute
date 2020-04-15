package co.atlvntis.tempmute.command;

import co.atlvntis.tempmute.TempmutePlugin;
import co.atlvntis.tempmute.cache.UserCache;
import co.atlvntis.tempmute.entity.User;
import co.atlvntis.tempmute.entity.impl.UserImpl;
import co.atlvntis.tempmute.repository.UserRepository;
import co.atlvntis.tempmute.util.MessageUtils;
import co.atlvntis.tempmute.util.TimeUtils;
import me.saiintbrisson.commands.Execution;
import me.saiintbrisson.commands.annotations.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class TempmuteCommand {

   private final TempmutePlugin plugin = TempmutePlugin.getInstance();
   private final UserRepository repository = plugin.getUserRepository();
   private final UserCache cache = plugin.getUserCache();
   private final FileConfiguration configuration = plugin.getConfig();

   @Command(name = "tempmute")
   public void tempMute(Execution execution) {

      CommandSender sender = execution.getSender();
      if(!sender.hasPermission("senior.tempmute")) {
         sender.sendMessage(MessageUtils.colorize(configuration.getStringList("messages.no_permission")));
         return;
      }

      String[] arguments = execution.getArgs();
      if(arguments.length != 2) {
         sender.sendMessage(MessageUtils.colorize(configuration.getStringList("messages.tempmute_usage")));
         return;
      }

      Player target = Bukkit.getPlayer(arguments[0]);

      if(target == null || !target.isOnline()) {
         sender.sendMessage(
                 MessageUtils.colorize(configuration.getStringList("messages.player_not_found"))
                         .replace("@player", arguments[0])
         );
         return;
      }

      if(target == sender) {
         sender.sendMessage(MessageUtils.colorize(configuration.getStringList("messages.cannot_mute_yourself")));
         return;
      }

      long time = TimeUtils.millisFromString(arguments[1]);

      if(time == -1) {
         sender.sendMessage(MessageUtils.colorize(configuration.getStringList("messages.tempmute_usage")));
         sender.sendMessage(MessageUtils.colorize(configuration.getStringList("messages.time_examples")));
         return;
      }

      User user = cache.find(target.getUniqueId());

      if(user == null) user = UserImpl.builder()
              .uuid(target.getUniqueId())
              .unmuteTime(-1)
              .build();

      long unmuteTime = time + System.currentTimeMillis();

      if(user.isMuted() && user.getUnmuteTime() > unmuteTime) {
         sender.sendMessage(MessageUtils.colorize(configuration.getStringList("messages.already_muted")));
         return;
      }

      final User finalUser = user;
      user.setUnmuteTime(unmuteTime);

      Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
         cache.insert(finalUser);
         repository.insert(finalUser);
         sender.sendMessage(
                 MessageUtils.colorize(configuration.getStringList("messages.you_have_muted"))
                         .replace("@player", target.getName())
                         .replace("@time", TimeUtils.format(time))
         );
         target.sendMessage(
                 MessageUtils.colorize(configuration.getStringList("messages.you_have_been_muted"))
                         .replace("@player", sender.getName())
                         .replace("@time", TimeUtils.format(time))
         );
      });

   }

}
