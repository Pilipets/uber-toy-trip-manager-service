package uber.trip_manager_service.structures.internal;

import uber.trip_manager_service.structures.external.GeoPoint;

import java.util.UUID;

public class TripForDriver {
   final UUID clientId, tripId;
   final GeoPoint toLocation;

   public TripForDriver(UUID clientId, UUID tripId, GeoPoint toLocation) {
      this.clientId = clientId;
      this.tripId = tripId;
      this.toLocation = toLocation;
   }
}
