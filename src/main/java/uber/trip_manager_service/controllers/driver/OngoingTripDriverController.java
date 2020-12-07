package uber.trip_manager_service.controllers.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import uber.trip_manager_service.clients.ClientsWrapper;
import uber.trip_manager_service.services.driver.OngoingTripDriverService;
import uber.trip_manager_service.structures.internal.TripForDriver;

import java.util.UUID;
import java.util.concurrent.ForkJoinPool;

@RestController(value = "ongoing-driver")
@RequestMapping(path="/driver")
public class OngoingTripDriverController {
   private final ClientsWrapper clientsWrapper;
   private final OngoingTripDriverService driverService;

   @Autowired
   public OngoingTripDriverController(
         final ClientsWrapper clientsWrapper,
         final OngoingTripDriverService driverService) {
      this.clientsWrapper = clientsWrapper;
      this.driverService = driverService;
   }

   @PostMapping(path="/cancel-trip")
   public DeferredResult<ResponseEntity<Object>> cancelTrip(
         @RequestParam(value = "driver_id") String driverId,
         @RequestParam(value = "trip_id") UUID tripId) {

      DeferredResult<ResponseEntity<Object>> output = new DeferredResult<>();

      ForkJoinPool.commonPool().submit(() -> {
         driverService.cancelTrip(output, driverId, tripId);
      });
      return output;
   }

   @PostMapping(path="/start-trip")
   public DeferredResult<ResponseEntity<TripForDriver>> startTrip(
         @RequestParam(value = "driver_id") String driverId,
         @RequestParam(value = "trip_id") UUID tripId) {

      DeferredResult<ResponseEntity<TripForDriver>> output = new DeferredResult<>();

      ForkJoinPool.commonPool().submit(() -> {
         driverService.startTrip(output, driverId, tripId);
      });
      return output;
   }

   @PostMapping(path="/complete-trip")
   public DeferredResult<ResponseEntity<Object>> completeTrip(
         @RequestParam(value = "driver_id") String driverId,
         @RequestParam(value = "trip_id") UUID tripId) {

      DeferredResult<ResponseEntity<Object>> output = new DeferredResult<>();

      ForkJoinPool.commonPool().submit(() -> {
         driverService.completeTrip(output, driverId, tripId);
      });
      return output;
   }
}
