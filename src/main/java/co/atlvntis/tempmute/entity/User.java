package co.atlvntis.tempmute.entity;

import java.util.UUID;

public interface User {

   UUID getUniqueID();

   void setUnmuteTime(long unmuteTime);
   long getUnmuteTime();

   boolean isMuted();

}
