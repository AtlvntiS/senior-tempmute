package co.atlvntis.tempmute.listener;

import co.atlvntis.tempmute.TempmutePlugin;
import co.atlvntis.tempmute.cache.UserCache;
import co.atlvntis.tempmute.entity.User;
import co.atlvntis.tempmute.util.MessageUtils;
import co.atlvntis.tempmute.util.TimeUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class ChatListener implements Listener {

   private final TempmutePlugin plugin = TempmutePlugin.getInstance();
   private final UserCache cache = plugin.getUserCache();
   private final FileConfiguration configuration = plugin.getConfig();

   @EventHandler( priority = EventPriority.MONITOR )
   public void onPlayerChat(AsyncPlayerChatEvent event) {

      Player player = event.getPlayer();
      UUID uuid = player.getUniqueId();
      User user = cache.find(uuid);

      if(user == null) return;
      if(!user.isMuted()) return;

      event.setCancelled(true);

      long remainingTime = user.getUnmuteTime() - System.currentTimeMillis();

      player.sendMessage(
              MessageUtils.colorize(configuration.getStringList("messages.you_are_muted"))
                      .replace("@remainingTime", TimeUtils.format(remainingTime))
      );

   }

}
