package uber.trip_manager_service.structures.internal;

import java.util.UUID;

public class TripForDriver {
   final UUID clientId, tripId;
   final LocationPoint toPoint;

   public TripForDriver(UUID clientId, UUID tripId, LocationPoint toPoint) {
      this.clientId = clientId;
      this.tripId = tripId;
      this.toPoint = toPoint;
   }

   public UUID getClientId() {
      return clientId;
   }

   public UUID getTripId() {
      return tripId;
   }

   public LocationPoint getToPoint() {
      return toPoint;
   }
}
