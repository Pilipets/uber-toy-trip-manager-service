package uber.location_service.structures;

public class SupplyReturnType {
   SupplyInstance supplyInstance = null;
   double distance;

   public SupplyInstance getSupplyInstance() {
      return supplyInstance;
   }

   public double getDistance() {
      return distance;
   }

   public SupplyReturnType() {

   }

   public SupplyReturnType(SupplyInstance supplyInstance, double distance) {
      this.supplyInstance = supplyInstance;
      this.distance = distance;
   }
}
