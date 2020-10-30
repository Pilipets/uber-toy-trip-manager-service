package geo.tests;

import geo.tests.utils.SupplyLocationImplTests;
import geo.tests.utils.RandomSupplyInstance;
import org.javatuples.Pair;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uber.location_service.structures.SupplyInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ClosestSupplyTest {
   SupplyLocationImplTests impl;
   ConcurrentHashMap<UUID, SupplyInstance> container;
   List<SupplyInstance> instances;
   ExecutorService service;

   @Before
   public void prepareInstances() {
      impl = new SupplyLocationImplTests();
      instances = new ArrayList<>();

      for (int i = 0; i < 1000; ++i) {
         SupplyInstance ins = RandomSupplyInstance.getRandomSupply();
         instances.add(ins);
         impl.update(ins);
      }
      container = impl.getContainerSet();
      service = impl.getThreadPool();
   }

   @After
   public void cleanUp() {
      impl = null;
      instances.clear();
      container = null;
      if (!service.isShutdown()) {
         service.shutdown();
         System.out.println("Running");
         try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }
      service = null;
   }

   @Test
   public void BasicTest() {
      long startTime = System.nanoTime();
      ConcurrentHashMap<UUID, SupplyInstance> container = impl.getContainerSet();
      Assert.assertEquals(impl.getContainerSet().size(), 1000);
      for (SupplyInstance ins : instances) {
         Assert.assertTrue(container.contains(ins));
      }
      long elapsedTime = System.nanoTime() - startTime;
      System.out.println("Total execution time in millis: " + elapsedTime/1000000);
   }

   @Test
   public void BasicTest2() {
      long startTime = System.nanoTime();
      for (int i = 0; i < 1000; ++i) {
         RandomSupplyInstance ins = (RandomSupplyInstance)instances.get(i);
         ins.resetLocation();
         Assert.assertEquals(ins, container.get(ins.getUUID()));
         impl.update(ins);
         ins = RandomSupplyInstance.getRandomSupply();
         impl.update(ins);
         Assert.assertNotEquals(container.get(ins.getUUID()), instances.get(i));
      }
      long elapsedTime = System.nanoTime() - startTime;
      System.out.println("Total execution time in millis: " + elapsedTime/1000000);
   }

   @Test
   public void BasicTest3() {
      long startTime = System.nanoTime();
      for (SupplyInstance ins : instances) {
         impl.getRadiusSupply(ins.getLocation());
      }
      long elapsedTime = System.nanoTime() - startTime;
      System.out.println("Total execution time in millis: " + elapsedTime/1000000);
   }

   @Test
   public void Test4() {
      long startTime = System.nanoTime();

      for (SupplyInstance ins : instances) {
         SupplyInstance rand = RandomSupplyInstance.getRandomSupply();
         List<Pair<SupplyInstance, Double>> res = impl.getRadiusSupply(rand.getLocation());
         List<Pair<SupplyInstance, Double>> res2 = impl.getClosestSupply(rand.getLocation());
         if (res != null && !res.isEmpty()) {
            System.out.println(res.get(0).getValue1());
            System.out.println(res2.size());
         }
      }
      long elapsedTime = System.nanoTime() - startTime;
      System.out.println("Total execution time in millis: " + elapsedTime/1000000);
   }
}
