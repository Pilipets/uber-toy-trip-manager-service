package uber.trip_manager_service.structures.internal;

import uber.trip_manager_service.structures.external.GeoPoint;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class TripEntity {
   final UUID clientId, tripId;
   final GeoPoint where, to;
   final Timestamp timestamp;

   UUID driverId;
   Double distance;

   public TripEntity(UUID clientId, GeoPoint where, GeoPoint to) {
      this.clientId = clientId;
      this.where = where;
      this.to = to;
      tripId = UUID.randomUUID();
      timestamp = new Timestamp(new Date().getTime());
   }

   public UUID getTripId() {
      return tripId;
   }
}
