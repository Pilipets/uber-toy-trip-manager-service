package uber.trip_manager_service.structures.external;

import uber.trip_manager_service.structures.external.GeoPoint;

import java.io.Serializable;
import java.util.UUID;

public class SupplyInstance implements Serializable {
   protected UUID id;
   protected GeoPoint location;

   public SupplyInstance() {
   }

   public UUID getId() {
      return id;
   }

   public GeoPoint getLocation() {
      return location;
   }
}
