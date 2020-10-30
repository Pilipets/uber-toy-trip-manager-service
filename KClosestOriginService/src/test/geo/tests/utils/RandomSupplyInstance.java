package geo.tests.utils;

import uber.location_service.structures.GeoPoint;
import uber.location_service.structures.SupplyInstance;

import java.util.UUID;

public class RandomSupplyInstance extends SupplyInstance {
   RandomSupplyInstance() {
      super();
      this.uuid = UUID.randomUUID();

      resetLocation();
   }

   public void resetLocation() {
      this.location = new GeoPoint(
            Math.toRadians(Math.random() * (180) - 90),
            Math.toRadians(Math.random() * (360) - 180));
   }

   public void setLocation(GeoPoint location) {
      this.location = location;
   }

   public static RandomSupplyInstance getRandomSupply() {
      RandomSupplyInstance ins = new RandomSupplyInstance();
      return ins;
   }
}
