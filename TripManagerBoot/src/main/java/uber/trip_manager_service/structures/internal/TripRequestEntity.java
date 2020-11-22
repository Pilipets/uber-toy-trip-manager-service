package uber.trip_manager_service.structures.internal;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import uber.trip_manager_service.structures.external.GeoPoint;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public class TripRequestEntity {
   RequestTripParams params;

   LocationPoint fromPoint, toPoint;
   UUID clientId;
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

   public UUID getClientId() {
      return clientId;
   }
}
