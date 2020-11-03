package uber.trip_manager_service.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uber.trip_manager_service.services.TripManagerImpl;
import uber.trip_manager_service.structures.GeoPoint;
import uber.trip_manager_service.structures.SupplyInstance;
import uber.trip_manager_service.structures.Trip;

import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping(path="/trip_manager_service")
public class TripManagerController {
   private final TripManagerImpl impl;

   @Autowired
   public TripManagerController(final TripManagerImpl impl) {
      this.impl = impl;
   }

   @PostMapping(path="/request_trip")
   public void requestTrip(@RequestParam(value = "uuid") UUID clientUUID,
                           @RequestParam(value = "where") String whereString,
                           @RequestParam(value = "to") String toString) throws JsonProcessingException {
      ObjectMapper mapper = new ObjectMapper();
      final GeoPoint where = mapper.readValue(whereString, GeoPoint.class),
            to = mapper.readValue(toString, GeoPoint.class);


      impl.newTrip(clientUUID, where, to);
   }

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
}
