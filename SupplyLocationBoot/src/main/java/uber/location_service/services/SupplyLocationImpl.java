package uber.location_service.services;

import org.javatuples.Pair;
import org.springframework.stereotype.Service;
import uber.location_service.algo.callable.ClosestSupplyCallable;
import uber.location_service.algo.callable.RadiusSupplyCallable;
import uber.location_service.structures.GeoPoint;
import uber.location_service.structures.SupplyInstance;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

@Service
public class SupplyLocationImpl {
   final int corePoolSize = 3;
   final int maximumPoolSize = 10;
   final int keepAliveTime = 120; // sec
   final int waitQueueCapacity = 1000;

   // In the future might be replaced with S2 library and KD-Tree
   // internal cache container
   protected ConcurrentHashMap<UUID, SupplyInstance> lhm = new ConcurrentHashMap<>(100);
   protected ThreadPoolExecutor executorService;

   public SupplyLocationImpl() {
      executorService = new ThreadPoolExecutor(
            corePoolSize,
            maximumPoolSize,
            keepAliveTime,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(waitQueueCapacity)
      );
   }

   public void update(SupplyInstance val) {
      lhm.put(val.getUUID(), val);
   }

   public SupplyInstance get(UUID uuid) {
      return lhm.getOrDefault(uuid, null);
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
