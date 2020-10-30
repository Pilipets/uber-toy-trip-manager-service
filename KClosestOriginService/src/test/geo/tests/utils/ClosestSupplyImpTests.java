package geo.tests.utils;

import uber.location_service.ClosestSupplyImpl;
import uber.location_service.structures.SupplyInstance;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

public class ClosestSupplyImpTests extends ClosestSupplyImpl {

   public ThreadPoolExecutor getThreadPool() {
      return this.executorService;
   }

   public ConcurrentHashMap<UUID, SupplyInstance> getContainerSet() {
      return this.lhm;
   }
}
