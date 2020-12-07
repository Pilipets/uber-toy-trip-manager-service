package uber.trip_manager_service.structures.internal;

import uber.trip_manager_service.structures.external.GeoPoint;

import java.io.Serializable;

class Address {
   String addressFull;

   public Address() {

   }
   public String getAddressFull() {
      return addressFull;
   }
}

public class LocationPoint {
   GeoPoint location;
   Address address;

   public LocationPoint() {

   }
   public Address getAddress() { return address; }
   public GeoPoint getLocation() {
      return location;
   }

}