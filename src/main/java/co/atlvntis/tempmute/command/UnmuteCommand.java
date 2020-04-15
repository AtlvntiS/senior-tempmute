package co.atlvntis.tempmute.command;

import co.atlvntis.tempmute.TempmutePlugin;
import co.atlvntis.tempmute.cache.UserCache;
import co.atlvntis.tempmute.entity.User;
import co.atlvntis.tempmute.repository.UserRepository;
import co.atlvntis.tempmute.util.MessageUtils;
import co.atlvntis.tempmute.util.TimeUtils;
import me.saiintbrisson.commands.Execution;
import me.saiintbrisson.commands.annotations.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class UnmuteCommand {

   private final TempmutePlugin plugin = TempmutePlugin.getInstance();
   private final UserCache cache = plugin.getUserCache();
   private final UserRepository repository = plugin.getUserRepository();
   private final FileConfiguration configuration = plugin.getConfig();

   @Command(name = "unmute")
   public void unmute(Execution execution) {

      CommandSender sender = execution.getSender();
      if(!sender.hasPermission("senior.unmute")) {
         sender.sendMessage(MessageUtils.colorize(configuration.getStringList("messages.no_permission")));
         return;
      }

      String[] arguments = execution.getArgs();
      if(arguments.length != 1) {
         sender.sendMessage(MessageUtils.colorize(configuration.getStringList("messages.unmute_usage")));
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

      User user = cache.find(target.getUniqueId());

      if(user == null || !user.isMuted()) {
         sender.sendMessage(MessageUtils.colorize(configuration.getStringList("messages.not_muted")));
         return;
      }

      user.setUnmuteTime(-1);

      Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
         cache.delete(user.getUniqueID());
         repository.delete(user.getUniqueID());
         sender.sendMessage(
                 MessageUtils.colorize(configuration.getStringList("messages.you_have_unmuted"))
                         .replace("@player", target.getName())
         );
         target.sendMessage(
                 MessageUtils.colorize(configuration.getStringList("messages.you_have_been_unmuted"))
                         .replace("@player", sender.getName())
         );
      });

   }

}
