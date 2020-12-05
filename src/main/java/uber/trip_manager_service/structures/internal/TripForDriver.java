package uber.trip_manager_service.structures.internal;

import java.util.UUID;

public class TripForDriver {
   final UUID driverId, tripId;
   final LocationPoint toPoint;

   public TripForDriver(UUID clientId, UUID tripId, LocationPoint toPoint) {
      this.driverId = clientId;
      this.tripId = tripId;
      this.toPoint = toPoint;
   }

   public UUID getDriverId() {
      return driverId;
   }

   public UUID getTripId() {
      return tripId;
   }

   public LocationPoint getToPoint() {
      return toPoint;
   }
}
