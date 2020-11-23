package uber.location_service.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import uber.location_service.algo.callable.ClosestSupplyCallable;
import uber.location_service.algo.callable.RadiusSupplyCallable;
import uber.location_service.structures.GeoPoint;
import uber.location_service.structures.SupplyInstance;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

@Service
public class SupplyLocationImpl {
   private final int corePoolSize = 3;
   private final int maximumPoolSize = 10;
   private final int keepAliveTime = 120; // sec
   private final int waitQueueCapacity = 1000;

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

   public void  updateSupply(SupplyInstance val) {
      lhm.put(val.getId(), val);
   }

   public SupplyInstance getSupply(UUID id) {
      return lhm.getOrDefault(id, null);
   }

   public List<SupplyInstance> getRadiusSupply(GeoPoint location) {
      try {
         Future<List<SupplyInstance>> future =
               executorService.submit(new RadiusSupplyCallable(lhm, location));
         return future.get();
      } catch (RejectedExecutionException | InterruptedException | ExecutionException ex) {
         ex.printStackTrace();
         return null;
      }
   }

   public List<SupplyInstance> getTestSupply(GeoPoint location) {
      DeferredResult<ResponseEntity<List<SupplyInstance>>> output = new DeferredResult<>();
      try {
         executorService.submit()
         Future<List<SupplyInstance>> future =
               executorService.submit(new RadiusSupplyCallable(lhm, location));
         return future.get();
      } catch (RejectedExecutionException | InterruptedException | ExecutionException ex) {
         ex.printStackTrace();
         return null;
      }
   }
   public List<SupplyInstance> getClosestSupply(GeoPoint location) {
      try {
         Future<List<SupplyInstance>> future =
               executorService.submit(new ClosestSupplyCallable(lhm, location));
         return future.get();
      } catch (RejectedExecutionException | InterruptedException | ExecutionException ex) {
         ex.printStackTrace();
         return null;
      }
   }

   public GeoPoint  getSupplyLocation(UUID id) {
      SupplyInstance ins = lhm.getOrDefault(id, null);
      if (ins == null) {
         return null;
      }
      return ins.getLocation();
   }
}
