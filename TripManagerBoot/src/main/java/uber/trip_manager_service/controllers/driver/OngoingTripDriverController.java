package uber.trip_manager_service.controllers.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uber.trip_manager_service.services.OngoingTripService;
import uber.trip_manager_service.structures.internal.TripForDriver;

import java.util.UUID;

@RestController(value = "ongoing-driver")
@RequestMapping(path="/trip-manager-service/driver")
public class OngoingTripDriverController {
   private final OngoingTripService impl;

   @Autowired
   public OngoingTripDriverController(final OngoingTripService impl) {
      this.impl = impl;
   }

   @PostMapping(path="/cancel-trip")
   public ResponseEntity<Object> cancelTripDriver(
         @RequestParam(value = "driver_id") UUID driverId,
         @RequestParam(value = "trip_id") UUID tripId) {

      return impl.cancelTripDriver(driverId, tripId);
   }

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
   }
}
