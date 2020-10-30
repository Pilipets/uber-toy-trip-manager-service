package uber.location_service.algo.callable;

import org.javatuples.Pair;
import uber.location_service.algo.GeoAlgorithms;
import uber.location_service.structures.GeoPoint;
import uber.location_service.structures.SupplyInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class RadiusSupplyCallable implements Callable<List<Pair<SupplyInstance, Double>>> {
   private final static double earthRadius = 6371.01; //km
   private final static double maxSearchDistance = 15.0; // km
   private final static double minSearchDistance = 4.0; // km


   private final GeoPoint location;
   private final ConcurrentHashMap<UUID, SupplyInstance> lhm;
   public RadiusSupplyCallable(
         ConcurrentHashMap<UUID, SupplyInstance> lhm, GeoPoint location) {
      this.lhm = lhm;
      this.location = location;
   }

   @Override
   public List<Pair<SupplyInstance, Double>> call() {
      List<Pair<SupplyInstance, Double>> radiusSupplySet = new ArrayList<>();
      double curDistance = minSearchDistance;

      while (curDistance <= maxSearchDistance && radiusSupplySet.isEmpty()) {
         radiusSupplySet = GeoAlgorithms.findPlacesWithinDistance(
               lhm.entrySet().iterator(), earthRadius, location, curDistance);
         curDistance *= 2;
      }

      return radiusSupplySet;
   }
}
