package co.atlvntis.tempmute.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Statement;

@Getter
@RequiredArgsConstructor
public class MySQL {

   private static final String CREATE_MUTES_TABLE = "" +
           "CREATE TABLE IF NOT EXISTS mutes (" +
           "id VARCHAR(36) NOT NULL PRIMARY KEY," +
           "unmute_time BIGINT NOT NULL" +
           ");";

   private final String url;
   private final String username;
   private final String password;

   private HikariDataSource dataSource;

   public void connect() {

      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(url);
      config.setUsername(username);
      config.setPassword(password);

      this.dataSource = new HikariDataSource(config);
      createTables();

   }

   private void createTables() {

      try (Statement statement = this.dataSource.getConnection().createStatement()) {
         statement.executeUpdate(CREATE_MUTES_TABLE);
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

}
