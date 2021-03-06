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

   final String clientId;
   final UUID tripId;
   final LocationPoint fromPoint, toPoint;
   Timestamp fromTimestamp, toTimestamp;
   TripStatus status;

   String driverId;
   double distance;
   double price;

   public TripForDB(String clientId, LocationPoint fromPoint, LocationPoint toPoint) {
      this.clientId = clientId;
      this.fromPoint = fromPoint;
      this.toPoint = toPoint;

      this.tripId = UUID.randomUUID();
      this.status = TripStatus.REQUESTED;
   }


   public UUID getTripId() {
      return tripId;
   }

   public String getDriverId() {
      return driverId;
   }

   public LocationPoint getFromPoint() {
      return fromPoint;
   }

   public LocationPoint getToPoint() { return toPoint; }

   public String getClientId() {
      return clientId;
   }

   public TripStatus getStatus() {
      return status;
   }

   public void setAccepted(String driverId) {
      this.driverId = driverId;
      status = TripStatus.ACCEPTED;
      fromTimestamp = new Timestamp(new Date().getTime());
   }

   public void setCancelled() {
      status = TripStatus.CANCELLED;
      toTimestamp = new Timestamp(new Date().getTime());
   }

   public void setStarted() {
      status = TripStatus.IN_PROGRESS;
   }

   public void setCompleted() {
      status = TripStatus.COMPLETED;
      toTimestamp = new Timestamp(new Date().getTime());
   }
}
