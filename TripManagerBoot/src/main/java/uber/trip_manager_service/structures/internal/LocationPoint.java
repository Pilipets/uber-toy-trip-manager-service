package uber.trip_manager_service.structures.internal;

import uber.trip_manager_service.structures.external.GeoPoint;

import java.io.Serializable;

class Address implements Serializable{
   String addressFull;
   public String getAddressFull() {
      return addressFull;
   }
}

public class LocationPoint implements Serializable {
   GeoPoint location;
   Address address;

   public GeoPoint getLocation() {
      return location;
   }

}