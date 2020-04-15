package co.atlvntis.tempmute;

import co.atlvntis.tempmute.cache.UserCache;
import co.atlvntis.tempmute.command.TempmuteCommand;
import co.atlvntis.tempmute.command.UnmuteCommand;
import co.atlvntis.tempmute.database.MySQL;
import co.atlvntis.tempmute.listener.ChatListener;
import co.atlvntis.tempmute.listener.TrafficListener;
import co.atlvntis.tempmute.repository.UserRepository;
import lombok.Getter;
import me.saiintbrisson.commands.CommandFrame;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class TempmutePlugin extends JavaPlugin {

   private MySQL mySQL;

   private UserRepository userRepository;
   private UserCache userCache;

   private CommandFrame commandFrame;

   @Override
   public void onEnable() {

      setupConfig();
      if(!setupMySQL())
         return;

      this.userRepository = new UserRepository();
      this.userCache = new UserCache();

      registerListeners();
      registerCommands();

   }

   @Override
   public void onDisable() {

   }

   private void setupConfig() {
      if(!getDataFolder().exists())
         getDataFolder().mkdirs();

      saveDefaultConfig();
   }

   private boolean setupMySQL() {

      FileConfiguration configuration = getConfig();

      String url = configuration.getString("database.url");
      String username = configuration.getString("database.username");
      String password = configuration.getString("database.password");

      if(url == null || username == null || password == null) return false;

      mySQL = new MySQL(url, username, password);
      mySQL.connect();
      return true;

   }

   private void registerListeners() {
      PluginManager pluginManager = getServer().getPluginManager();
      pluginManager.registerEvents(new TrafficListener(), this);
      pluginManager.registerEvents(new ChatListener(), this);
   }

   private void registerCommands() {
      commandFrame = new CommandFrame(this);
      commandFrame.registerCommands(new TempmuteCommand(), new UnmuteCommand());
   }

   public static TempmutePlugin getInstance() {
      return getPlugin(TempmutePlugin.class);
   }

}
