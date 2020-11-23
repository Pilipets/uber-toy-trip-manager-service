package uber.trip_manager_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uber.trip_manager_service.services.BeforeTripService;
import uber.trip_manager_service.services.OngoingTripService;
import uber.trip_manager_service.structures.internal.TripForDriver;

import java.util.UUID;

@RestController()
@RequestMapping(path="/trip-manager-service")
public class OngoingTripController {
   private final OngoingTripService impl;

   @Autowired
   public OngoingTripController(final OngoingTripService impl) {
      this.impl = impl;
   }

   @PostMapping(path="/cancel-trip-client")
   public ResponseEntity<Object> cancelTripClient(
         @RequestParam(value = "client_id") UUID clientId,
         @RequestParam(value = "trip_id") UUID tripId) {

      return impl.cancelTripClient(clientId, tripId);
   }

   @PostMapping(path="/cancel-trip-driver")
   public ResponseEntity<Object> cancelTripDriver(
         @RequestParam(value = "driver_id") UUID driverId,
         @RequestParam(value = "trip_id") UUID tripId) {

      return impl.cancelTripDriver(driverId, tripId);
   }

   @PostMapping(path="/start-trip-driver")
   public ResponseEntity<TripForDriver> startTripDriver(
         @RequestParam(value = "driver_id") UUID driverId,
         @RequestParam(value = "trip_id") UUID tripId) {

      return impl.startTripDriver(driverId, tripId);
   }

   @PostMapping(path="/complete-trip")
   public ResponseEntity<Object> completeTrip(
         @RequestParam(value = "driver_uuid") UUID driverId,
         @RequestParam(value = "trip_uuid") UUID tripId) {

      return impl.completeTripDriver(driverId, tripId);
   }
}
