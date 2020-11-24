package uber.trip_manager_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "proxy-service", url = "http://localhost:8078/proxy-service")
interface ProxyClient {
   // Goes to the driversService
   @PostMapping(path="/send-trip")
   ResponseEntity<Object> sendDriversTripPush(
         @RequestParam(value = "forward")String forward,
         @RequestBody Map<String, Object> driversTripPush);

   @PostMapping(path = "/trip-completed")
   ResponseEntity<Object> tripCompleted(
         @RequestParam(value = "forward") String forward,
         @RequestParam(value = "client_id") UUID clientId,
         @RequestParam(value = "trip_id") UUID tripId);


   /**
    * Trip cancelled by driver/client/internal error
    */
   @PostMapping(path = "/trip-cancelled")
   ResponseEntity<Object> tripCancelled(
         @RequestParam(value = "forward")String forward,
         @RequestParam(value = "id") UUID id,
         @RequestParam(value = "trip_id") UUID tripId);

   /**
    * Trip was accepted by the driver, notify the client service
    */
   @PostMapping(path = "/trip-accepted")
   ResponseEntity<Object> tripAccepted(
         @RequestParam(value = "forward") String forward,
         @RequestParam(value = "client_id") UUID clientId,
         @RequestBody Map<String, Object> tripInfo);
}
