package uber.location_service.utils;

import uber.location_service.geo.GeoPoint;

import java.util.UUID;

public class SupplyInstance {
   UUID uuid;
   GeoPoint location;

   SupplyInstance() {
   }

   @Override
   public int hashCode() {
      return uuid.hashCode();
   }

   @Override
   public boolean equals(Object obj) {
      // TODO Auto-generated method stub
      Person u=(Person) obj;

      return u.firstname.equals(firstname) && u.lastname.equals(lastname);
   }
}
