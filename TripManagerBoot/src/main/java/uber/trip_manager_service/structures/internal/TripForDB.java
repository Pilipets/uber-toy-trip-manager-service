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
   final GeoPoint fromLocation, toLocation;
   Timestamp fromTimestamp, toTimestamp;
   TripStatus status;

   UUID driverId;
   Double distance;

   public TripForDB(UUID clientId, GeoPoint fromLocation, GeoPoint toLocation) {
      this.clientId = clientId;
      this.fromLocation = fromLocation;
      this.toLocation = toLocation;
      this.fromTimestamp = null;
      this.tripId = UUID.randomUUID();
      this.status = TripStatus.REQUESTED;
   }

   public void setStarted() {
      fromTimestamp = new Timestamp(new Date().getTime());
   }

   public void setFinished() {
      toTimestamp = new Timestamp(new Date().getTime());
   }
   public UUID getTripId() {
      return tripId;
   }

   public UUID getDriverId() {
      return driverId;
   }

   public GeoPoint getFromLocation() {
      return fromLocation;
   }

   public GeoPoint getToLocation() { return toLocation; }

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
