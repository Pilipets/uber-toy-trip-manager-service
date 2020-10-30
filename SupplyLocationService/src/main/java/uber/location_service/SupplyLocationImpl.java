package uber.location_service;

import org.javatuples.Pair;
import uber.location_service.structures.GeoPoint;
import uber.location_service.utils.ClosestSupplyCallable;
import uber.location_service.utils.RadiusSupplyCallable;
import uber.location_service.structures.SupplyInstance;

import java.util.*;
import java.util.concurrent.*;

public class SupplyLocationImpl {
   // In the future might be replaced with S2 library and KD-Tree
   // internal cache container
   protected ConcurrentHashMap<UUID, SupplyInstance> lhm = new ConcurrentHashMap<>(100);
   protected ThreadPoolExecutor executorService;

   public SupplyLocationImpl() {
      executorService = new ThreadPoolExecutor(
            3,
            10,
            120,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000)
      );
   }

   public void update(SupplyInstance val) {
      lhm.put(val.getUUID(), val);
   }


   public List<Pair<SupplyInstance, Double>> getRadiusSupply(GeoPoint location) {
      try {
         Future<List<Pair<SupplyInstance, Double>>> future =
               executorService.submit(new RadiusSupplyCallable(lhm, location));
         return future.get();
      } catch (RejectedExecutionException | InterruptedException | ExecutionException ex) {
         ex.printStackTrace();
         return null;
      }
   }

   public List<Pair<SupplyInstance, Double>> getClosestSupply(GeoPoint location) {
      try {
         Future<List<Pair<SupplyInstance, Double>>> future =
               executorService.submit(new ClosestSupplyCallable(lhm, location));
         return future.get();
      } catch (RejectedExecutionException | InterruptedException | ExecutionException ex) {
         ex.printStackTrace();
         return null;
      }
   }

   public GeoPoint getSupplyLocation(UUID uuid) {
      return lhm.get(uuid).getLocation();
   }
}
