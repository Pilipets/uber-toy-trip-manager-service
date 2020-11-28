package uber.trip_manager_service.controllers.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import uber.trip_manager_service.services.client.BeforeTripClientService;
import uber.trip_manager_service.structures.internal.TripRequestEntity;

import java.util.concurrent.ForkJoinPool;

@RestController(value = "before-client")
@RequestMapping(path="/trip-manager-service/client")
public class BeforeTripClientController {
   private final BeforeTripClientService clientService;

   @Autowired
   public BeforeTripClientController(
         final BeforeTripClientService clientService) {
      this.clientService = clientService;
   }

   @PostMapping(path="/request-trip")
   public DeferredResult<ResponseEntity<Object>> requestTrip(
         @RequestBody TripRequestEntity tripRequestEntity) {

      DeferredResult<ResponseEntity<Object>> output = new DeferredResult<>();

      ForkJoinPool.commonPool().submit(() -> {
         clientService.requestNewTrip(output, tripRequestEntity);
      });

      return output;
   }
}
