package uber.trip_manager_service.structures.internal;

import uber.trip_manager_service.structures.external.GeoPoint;

import java.util.UUID;

class RequestTripParams {
   int capacity = 0;
   int typeId = 0;

   public int getCapacity() {
      return capacity;
   }

   public int getTypeId() {
      return typeId;
   }

   public RequestTripParams() {

   }

   public RequestTripParams(RequestTripParams other) {
      this.capacity = other.capacity;
      this.typeId = other.typeId;
   }
}

public class TripRequestEntity {
   RequestTripParams params;
   GeoPoint from, to;
   UUID clientId;

   public RequestTripParams getParams() {
      return params;
   }

   public GeoPoint getFrom() {
      return from;
   }

   public GeoPoint getTo() {
      return to;
   }

   public UUID getClientId() {
      return clientId;
   }
}
