package uber.trip_manager_service.controllers.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import uber.trip_manager_service.services.OngoingTripService;
import uber.trip_manager_service.services.driver.OngoingTripDriverService;
import uber.trip_manager_service.structures.internal.TripForDriver;

import java.util.UUID;
import java.util.concurrent.ForkJoinPool;

@RestController(value = "ongoing-driver")
@RequestMapping(path="/trip-manager-service/driver")
public class OngoingTripDriverController {
   private final OngoingTripDriverService driverService;

   @Autowired
   public OngoingTripDriverController(
         final OngoingTripDriverService driverService) {
      this.driverService = driverService;
   }

   @PostMapping(path="/cancel-trip")
   public DeferredResult<ResponseEntity<Object>> cancelTrip(
         @RequestParam(value = "driver_id") UUID driverId,
         @RequestParam(value = "trip_id") UUID tripId) {

      DeferredResult<ResponseEntity<Object>> output = new DeferredResult<>();

      ForkJoinPool.commonPool().submit(() -> {
         driverService.cancelTrip(output, driverId, tripId);
      });
      return output;
   }

   /*
   @PostMapping(path="/start-trip")
   public ResponseEntity<TripForDriver> startTripDriver(
         @RequestParam(value = "driver_id") UUID driverId,
         @RequestParam(value = "trip_id") UUID tripId) {

      return impl.startTripDriver(driverId, tripId);
   }

   @PostMapping(path="/complete-trip")
   public ResponseEntity<Object> completeTripFromDriver(
         @RequestParam(value = "driver_uuid") UUID driverId,
         @RequestParam(value = "trip_uuid") UUID tripId) {

      return impl.completeTripDriver(driverId, tripId);
   }

   @PostMapping(path="/update-trip")
   public ResponseEntity<TripForDriver> updateTripForClient(
         @RequestParam(value = "driver_id") UUID driverId,
         @RequestParam(value = "trip_id") UUID tripId) {

      return impl.startTripDriver(driverId, tripId);
   } */
}
