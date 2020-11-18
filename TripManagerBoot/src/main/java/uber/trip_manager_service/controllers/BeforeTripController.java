package uber.trip_manager_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uber.trip_manager_service.services.BeforeTripService;
import uber.trip_manager_service.structures.internal.TripForDriver;
import uber.trip_manager_service.structures.internal.TripRequestEntity;

import java.util.UUID;

@RestController()
@RequestMapping(path="/trip_manager_service")
public class BeforeTripController {
   private final BeforeTripService impl;

   @Autowired
   public BeforeTripController(final BeforeTripService impl) {
      this.impl = impl;
   }

   @PostMapping(path="/request_trip")
   public ResponseEntity<Object> temp(
         @RequestBody TripRequestEntity tripRequestEntity) {

      return impl.newTripRequest(tripRequestEntity);
   }

   @PostMapping(path="/accept_trip")
   public ResponseEntity<TripForDriver> acceptTrip(
         @RequestParam(value = "driver_id") UUID driverId,
         @RequestParam(value = "trip_id") UUID tripId) {

      return impl.acceptTrip(driverId, tripId);
   }
}
