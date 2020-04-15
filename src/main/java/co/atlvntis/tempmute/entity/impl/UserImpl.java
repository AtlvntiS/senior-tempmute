package co.atlvntis.tempmute.entity.impl;

import co.atlvntis.tempmute.entity.User;
import lombok.Builder;
import lombok.Setter;

import java.util.UUID;

@Setter
@Builder
public class UserImpl implements User {

   private final UUID uuid;
   private long unmuteTime;

   @Override
   public UUID getUniqueID() {
      return uuid;
   }

   @Override
   public long getUnmuteTime() {
      return unmuteTime;
   }

   @Override
   public boolean isMuted() {
      return unmuteTime > System.currentTimeMillis();
   }
}
