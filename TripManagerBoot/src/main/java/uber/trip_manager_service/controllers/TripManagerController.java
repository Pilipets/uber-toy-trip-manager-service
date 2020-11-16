package uber.trip_manager_service.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import uber.trip_manager_service.services.TripManagerImpl;
import uber.trip_manager_service.structures.internal.FilterTripParams;
import uber.trip_manager_service.structures.external.GeoPoint;
import uber.trip_manager_service.structures.internal.TripRequestEntity;

import java.util.Map;
import java.util.UUID;

@RestController()
@RequestMapping(path="/trip_manager_service")
public class TripManagerController {
   private final TripManagerImpl impl;
   private final ObjectMapper jsonMapper = new ObjectMapper();

   @Autowired
   public TripManagerController(final TripManagerImpl impl) {
      this.impl = impl;
   }

   @PostMapping(path="/request_trip")
   public ResponseEntity<Object> temp(@RequestBody TripRequestEntity tripRequestEntity) {

      return impl.newTripRequest(tripRequestEntity);
   }
/*
   @PostMapping(path="/accept_trip")
   public void acceptTrip(@RequestParam(value = "driver_uuid") UUID driverUUID,
                          @RequestParam(value = "trip_uuid") UUID tripUUID) {

      impl.acceptTrip(driverUUID, tripUUID);
   }

   @PostMapping(path="/cancel_trip")
   public void cancelTrip(@RequestParam(value = "uuid") UUID uuid,
                          @RequestParam(value = "trip_uuid") UUID tripUUID) {

      impl.cancelTrip(uuid, tripUUID);
   }

   @PostMapping(path="/complete_trip")
   public void completeTrip(@RequestParam(value = "driver_uuid") UUID driverUUID,
                            @RequestParam(value = "trip_uuid") UUID tripUUID) {

      impl.completeTrip(driverUUID, tripUUID);
   }

   @GetMapping(path="/get_trip_update")
   public void get_trip_update(@RequestParam(value = "driver_uuid") UUID clientUUID,
                               @RequestParam(value = "trip_uuid") UUID tripUUID) {

   }
   */
}
