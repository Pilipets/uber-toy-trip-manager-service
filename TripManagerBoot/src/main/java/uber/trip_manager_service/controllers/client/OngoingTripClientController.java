package uber.trip_manager_service.controllers.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import uber.trip_manager_service.services.OngoingTripService;
import uber.trip_manager_service.services.client.OngoingTripClientService;

import java.util.UUID;
import java.util.concurrent.ForkJoinPool;

@RestController(value = "ongoing-client")
@RequestMapping(path="/trip-manager-service/client")
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
}
