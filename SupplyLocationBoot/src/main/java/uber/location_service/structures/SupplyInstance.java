package uber.location_service.structures;

import java.io.Serializable;
import java.util.UUID;

public class SupplyInstance implements Serializable {
   protected UUID uuid;
   protected GeoPoint location;
   boolean onTrip;
   boolean acceptTrips;

   protected SupplyInstance() {
      this.onTrip = false;
      this.acceptTrips = true;
   }

   public UUID getUUID() {
      return uuid;
   }

   public GeoPoint getLocation() {
      return location;
   }

   public void setLocation(GeoPoint location) {
      this.location = location;
   }

   public boolean isOnTrip() {
      return onTrip;
   }

   public boolean isAcceptTrips() {
      return acceptTrips;
   }

   @Override
   public int hashCode() {
      return uuid.hashCode();
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      SupplyInstance other = (SupplyInstance) o;
      return uuid.equals(other.uuid) && location.equals(other.location);
   }
}
