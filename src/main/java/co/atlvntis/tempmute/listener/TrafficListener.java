package co.atlvntis.tempmute.listener;

import co.atlvntis.tempmute.TempmutePlugin;
import co.atlvntis.tempmute.cache.UserCache;
import co.atlvntis.tempmute.entity.User;
import co.atlvntis.tempmute.repository.UserRepository;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class TrafficListener implements Listener {

   private final TempmutePlugin plugin = TempmutePlugin.getInstance();
   private final UserRepository repository = plugin.getUserRepository();
   private final UserCache cache = plugin.getUserCache();

   @EventHandler
   public void onPlayerJoin(PlayerJoinEvent event) {
      Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

         Player player = event.getPlayer();
         UUID uuid = player.getUniqueId();
         User user = repository.find(uuid);

         if(user == null) return;

         cache.insert(user);

      });
   }

   @EventHandler
   public void onPlayerQuit(PlayerQuitEvent event) {
      Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

         Player player = event.getPlayer();
         UUID uuid = player.getUniqueId();
         User user = cache.find(uuid);

         if(user == null) return;
         if(user.isMuted()) {
            cache.delete(uuid);
            return;
         }

         repository.delete(uuid);
         cache.delete(uuid);

      });
   }

}
