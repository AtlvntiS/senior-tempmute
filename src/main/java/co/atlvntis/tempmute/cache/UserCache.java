package co.atlvntis.tempmute.cache;

import co.atlvntis.tempmute.entity.User;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.UUID;

public class UserCache {

   private final Cache<UUID, User> users;

   public UserCache() {
      this.users = CacheBuilder.newBuilder()
              .weakValues()
              .build();
   }

   public void insert(User user) {
      users.put(user.getUniqueID(), user);
   }

   public User find(UUID uuid) {
      return users.getIfPresent(uuid);
   }

   public void delete(UUID uuid) {
      users.invalidate(uuid);
   }

}
