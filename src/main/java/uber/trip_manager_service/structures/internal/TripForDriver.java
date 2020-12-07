package uber.trip_manager_service.structures.internal;

import java.util.UUID;

public class TripForDriver {
   final String driverId;
   final UUID tripId;
   final LocationPoint toPoint;

   public TripForDriver(String clientId, UUID tripId, LocationPoint toPoint) {
      this.driverId = clientId;
      this.tripId = tripId;
      this.toPoint = toPoint;
   }

   public String getDriverId() {
      return driverId;
   }

   public UUID getTripId() {
      return tripId;
   }

   public LocationPoint getToPoint() {
      return toPoint;
   }
}
