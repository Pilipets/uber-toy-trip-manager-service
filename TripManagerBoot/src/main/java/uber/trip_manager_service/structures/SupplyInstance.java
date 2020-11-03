package uber.trip_manager_service.structures;

import java.io.Serializable;
import java.util.UUID;

public class SupplyInstance implements Serializable {
   protected UUID uuid;
   protected GeoPoint location;

   protected SupplyInstance() {
   }

   public UUID getUUID() {
      return uuid;
   }

   public GeoPoint getLocation() {
      return location;
   }
}
