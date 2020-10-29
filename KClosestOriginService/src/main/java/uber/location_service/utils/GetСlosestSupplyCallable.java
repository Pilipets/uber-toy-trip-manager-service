package uber.location_service.utils;

import org.javatuples.Pair;
import uber.location_service.geo.GeoAlgorithms;
import uber.location_service.geo.GeoPoint;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class GetСlosestSupplyCallable<V> implements Callable<ArrayList<Pair<V, Double>>> {
   private final static double earthRadius = 6371.01; //km
   private final static double maxSearchDistance = 10.0; // km


   private final GeoPoint location;

   GetСlosestSupplyCallable(GeoPoint location) {
      this.location = location;
   }
   @Override
   public ArrayList<Pair<V, Double>> call() throws Exception {
      ArrayList closestSupplyArr = new ArrayList<Pair<V, Double>>();
      double curDistance = 2.0;

      while (curDistance <= maxSearchDistance && closestSupplyArr.isEmpty()) {
         closestSupplyArr = GeoAlgorithms.findPlacesWithinDistance(
               lhs.iterator(), earthRadius, location, curDistance);
         curDistance *= 2;
      }

      return closestSupplyArr;
   }
}
