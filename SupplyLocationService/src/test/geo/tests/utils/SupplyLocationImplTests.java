package geo.tests.utils;

import uber.location_service.SupplyLocationImpl;
import uber.location_service.structures.SupplyInstance;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

public class SupplyLocationImplTests extends SupplyLocationImpl {

   public ThreadPoolExecutor getThreadPool() {
      return this.executorService;
   }

   public ConcurrentHashMap<UUID, SupplyInstance> getContainerSet() {
      return this.lhm;
   }
}
