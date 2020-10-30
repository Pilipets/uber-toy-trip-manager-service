package uber.location_service.utils;

import org.javatuples.Pair;
import uber.location_service.algo.GeoAlgorithms;
import uber.location_service.structures.GeoPoint;
import uber.location_service.structures.SupplyInstance;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class ClosestSupplyCallable implements Callable<List<Pair<SupplyInstance, Double>>> {
   private final static double earthRadius = 6371.01; //km

   private final GeoPoint location;
   private final ConcurrentHashMap<UUID, SupplyInstance> lhm;
   public ClosestSupplyCallable(
         ConcurrentHashMap<UUID, SupplyInstance> lhm, GeoPoint location) {
      this.lhm = lhm;
      this.location = location;
   }

   @Override
   public List<Pair<SupplyInstance, Double>> call() {
      return GeoAlgorithms.getClosest(
            lhm.entrySet().iterator(), earthRadius, location);
   }
}
