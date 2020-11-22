package uber.trip_manager_service.structures.external;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
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
