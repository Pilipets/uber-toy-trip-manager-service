package uber.trip_manager_service.structures.internal;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import uber.trip_manager_service.structures.external.GeoPoint;

public class LocationPoint {
   class Address {
      public String getAddressFull() {
         return addressFull;
      }

      String addressFull;
   }

   public GeoPoint getLocation() {
      return location;
   }

   GeoPoint location;
}