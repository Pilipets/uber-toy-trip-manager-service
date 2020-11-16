package uber.trip_manager_service.structures.internal;

import uber.trip_manager_service.structures.external.GeoPoint;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class TripForDB {
   public enum  TripStatus {
      REQUESTED,
      ACCEPTED,
      IN_PROGRESS,
      COMPLETED,
      CANCELLED
   }

   final UUID clientId, tripId;
   final GeoPoint where, to;
   final Timestamp timestamp;
   TripStatus status;

   UUID driverId;
   Double distance;

   public TripForDB(UUID clientId, GeoPoint where, GeoPoint to) {
      this.clientId = clientId;
      this.where = where;
      this.to = to;
      tripId = UUID.randomUUID();
      timestamp = new Timestamp(new Date().getTime());
      status = TripStatus.REQUESTED;
   }

   public UUID getTripId() {
      return tripId;
   }

   public UUID getDriverId() {
      return driverId;
   }

   public UUID getClientId() {
      return clientId;
   }

   public TripStatus getStatus() {
      return status;
   }

   public void setDriverId(UUID driverId) {
      this.driverId = driverId;
   }

   public void setStatus(TripStatus status) {
      this.status = status;
   }
}
