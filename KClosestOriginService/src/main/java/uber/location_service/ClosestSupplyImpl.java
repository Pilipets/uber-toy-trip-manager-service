package uber.location_service;

import org.javatuples.Pair;
import uber.location_service.geo.GeoAlgorithms;
import uber.location_service.geo.GeoPoint;
import uber.location_service.utils.SupplyInstance;

import java.util.*;
import java.util.concurrent.*;

public class ClosestSupplyImpl<K,V> {
   final double earthRadius = 6371.01; //km
   final double maxSearchDistance = 10.0; // km

   LinkedHashSet lhs = new LinkedHashSet<SupplyInstance>(); // internal cache container

   ThreadPoolExecutor executorService;

   ClosestSupplyImpl() {
      executorService = new ThreadPoolExecutor(
            3,
            10,
            120,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(1000)
      );
   }

   private void update(V val) {
      lhs.add(val);
   }

   private void executeTask(Runnable task) {
      try {
         executorService.execute(task);
      } catch (RejectedExecutionException ex) {
         // service is bussy
      }
   }

   ArrayList<Pair<V, Double>> getClosestDemand(GeoPoint location) {
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
