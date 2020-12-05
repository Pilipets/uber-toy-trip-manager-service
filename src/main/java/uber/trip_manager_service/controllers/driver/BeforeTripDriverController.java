package uber.trip_manager_service.controllers.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import uber.trip_manager_service.services.driver.BeforeTripDriverService;
import uber.trip_manager_service.structures.internal.TripForDriver;
import uber.trip_manager_service.structures.internal.TripRequestEntity;

import java.util.UUID;
import java.util.concurrent.ForkJoinPool;

@RestController(value = "before-driver")
@RequestMapping(path="/driver")
public class BeforeTripDriverController {
   private final BeforeTripDriverService driverService;

   @Autowired
   public BeforeTripDriverController(final BeforeTripDriverService driverService) {
      this.driverService = driverService;
   }

   @PostMapping(path="/accept-trip")
   public DeferredResult<ResponseEntity<TripForDriver>> acceptTrip(
         @RequestParam(value = "driver_id") UUID driverId,
         @RequestParam(value = "trip_id") UUID tripId) {

      DeferredResult<ResponseEntity<TripForDriver>> output = new DeferredResult<>();

      ForkJoinPool.commonPool().submit(() -> {
         driverService.acceptTrip(output, driverId, tripId);
      });

      return output;
   }
}
