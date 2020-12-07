package uber.trip_manager_service.structures.internal;

import java.sql.Timestamp;
import java.util.UUID;

public class TripRequestEntity {
   RequestTripParams params;

   LocationPoint fromPoint, toPoint;
   String clientId;
   Timestamp timestamp;

   public RequestTripParams getParams() {
      return params;
   }

   public LocationPoint getFromPoint() {
      return fromPoint;
   }

   public LocationPoint getToPoint() {
      return toPoint;
   }

   public String getClientId() {
      return clientId;
   }
}
