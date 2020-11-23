package uber.trip_manager_service.structures.internal;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class TripForDB {
   public enum  TripStatus {
      REQUESTED(0),
      ACCEPTED(1),
      IN_PROGRESS(2),
      COMPLETED(3),
      CANCELLED(4);

      final int label;

      public int getLabel() {
         return label;
      }

      TripStatus(int label) {
         this.label = label;
      }
   }

   final UUID clientId;
   final UUID tripId;
   final LocationPoint fromPoint, toPoint;
   Timestamp fromTimestamp, toTimestamp;
   TripStatus status;

   UUID driverId;
   Double distance;
   Double price;

   public TripForDB(UUID clientId, LocationPoint fromPoint, LocationPoint toPoint) {
      this.clientId = clientId;
      this.fromPoint = fromPoint;
      this.toPoint = toPoint;

      this.tripId = UUID.randomUUID();
      this.status = TripStatus.REQUESTED;
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

   public LocationPoint getFromPoint() {
      return fromPoint;
   }

   public LocationPoint getToPoint() { return toPoint; }

   public UUID getClientId() {
      return clientId;
   }

   public TripStatus getStatus() {
      return status;
   }

   public void setOngoing() {
      status = TripStatus.IN_PROGRESS;
      fromTimestamp = new Timestamp(new Date().getTime());
   }

   public void setAccepted(UUID driverId) {
      this.driverId = driverId;
      status = TripStatus.ACCEPTED;
      fromTimestamp = new Timestamp(new Date().getTime());
   }
}
