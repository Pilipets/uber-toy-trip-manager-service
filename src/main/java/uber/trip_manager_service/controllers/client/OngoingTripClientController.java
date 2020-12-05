package uber.trip_manager_service.controllers.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import uber.trip_manager_service.services.client.OngoingTripClientService;
import uber.trip_manager_service.structures.external.GeoPoint;

import java.util.UUID;
import java.util.concurrent.ForkJoinPool;

@RestController(value = "ongoing-client")
@RequestMapping(path="/client")
public class OngoingTripClientController {
   private final OngoingTripClientService cLientService;

   @Autowired
   public OngoingTripClientController(
         final OngoingTripClientService cLientService) {
      this.cLientService = cLientService;
   }

   @PostMapping(path="/cancel-trip")
   public DeferredResult<ResponseEntity<Object>> cancelTrip(
         @RequestParam(value = "client_id") UUID clientId,
         @RequestParam(value = "trip_id") UUID tripId) {

      DeferredResult<ResponseEntity<Object>> output = new DeferredResult<>();

      ForkJoinPool.commonPool().submit(() -> {
         cLientService.cancelTrip(output, clientId, tripId);
      });

      return output;
   }

   @GetMapping(path="/get-driver-location")
   public DeferredResult<ResponseEntity<GeoPoint>> getDriverLocation(
         @RequestParam(value = "trip_id") UUID tripId) {

      DeferredResult<ResponseEntity<GeoPoint>> output = new DeferredResult<>();

      ForkJoinPool.commonPool().submit(() -> {
         cLientService.getDriverLocation(output, tripId);
      });

      return output;
   }
}
