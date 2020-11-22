package uber.trip_manager_service.structures.internal;

import uber.trip_manager_service.structures.external.GeoPoint;

import java.util.UUID;

public class TripForDriver {
   final UUID clientId, tripId;
   final LocationPoint toPoint;

   public TripForDriver(UUID clientId, UUID tripId, LocationPoint toPoint) {
      this.clientId = clientId;
      this.tripId = tripId;
      this.toPoint = toPoint;
   }
}
