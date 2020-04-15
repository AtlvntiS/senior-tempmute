package co.atlvntis.tempmute.repository;

import co.atlvntis.tempmute.TempmutePlugin;
import co.atlvntis.tempmute.entity.User;
import co.atlvntis.tempmute.entity.impl.UserImpl;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class UserRepository {

   private static final String FIND_USER = "SELECT * FROM mutes WHERE id = ? LIMIT 1;";
   private static final String INSERT_USER = "" +
           "INSERT INTO mutes (" +
           "id, unmute_time" +
           ") VALUES (" +
           "?, ?" +
           ") ON DUPLICATE KEY UPDATE " +
           "unmute_time = ?;";
   private static final String DELETE_USER = "DELETE FROM mutes WHERE id = ?;";

   private final TempmutePlugin plugin = TempmutePlugin.getInstance();
   private final HikariDataSource dataSource = plugin.getMySQL().getDataSource();

   public User find(UUID uuid) {
      try(Connection connection = dataSource.getConnection()) {

         PreparedStatement statement = connection.prepareStatement(FIND_USER);
         statement.setString(1, uuid.toString());

         ResultSet resultSet = statement.executeQuery();
         if(!resultSet.next()) return null;

         return UserImpl.builder()
                 .uuid(UUID.fromString(resultSet.getString("id")))
                 .unmuteTime(resultSet.getLong("unmute_time"))
                 .build();

      } catch (Exception exception) {
         return null;
      }
   }

   public void insert(User user) {
      try(Connection connection = dataSource.getConnection()) {

         PreparedStatement statement = connection.prepareStatement(INSERT_USER);

         String uuidString = user.getUniqueID().toString();
         long unmuteTime = user.getUnmuteTime();

         statement.setString(1, uuidString);
         statement.setLong(2, unmuteTime);
         statement.setLong(3, unmuteTime);

         statement.executeUpdate();

      } catch (Exception exception) {
         exception.printStackTrace();
      }
   }

   public void delete(UUID uuid) {
      try(Connection connection = dataSource.getConnection()) {

         PreparedStatement statement = connection.prepareStatement(DELETE_USER);

         String uuidString = uuid.toString();

         statement.setString(1, uuidString);

         statement.executeUpdate();

      } catch (Exception exception) {
         exception.printStackTrace();
      }
   }

}
