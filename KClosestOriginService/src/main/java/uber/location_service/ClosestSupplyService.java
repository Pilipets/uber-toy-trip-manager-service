package uber.location_service;

import uber.location_service.geo.GeoPoint;

class ClosestSupplyService {
   public static void main(String[] args) {
      System.out.println("Hello world");
      GeoPoint p = GeoPoint.fromRadians(0.23423, 32423.323);
      System.out.println(p);
   }

   ClosestSupplyImpl impl = new ClosestSupplyImpl();
}
