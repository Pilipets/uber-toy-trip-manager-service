package uber.location_service.structures;

import java.io.Serializable;

public class Temp implements Serializable {
   public Temp() {

   }
   Temp(double val1, double val2) {
      this.val1 = val1;
      this.val2 = val2;
   }

   public double getVal1() {
      return val1;
   }

   public double getVal2() {
      return val2;
   }

   double val1 = 0;

   double val2 = 0;
}
