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

@FeignClient(name = "proxy-service",
      url = "https://uber-proxy.herokuapp.com")
interface ProxyClient {
   // -------------------------- DRIVER ------------------------
   @PostMapping(path="/api/trip_broadcast")
   ResponseEntity<Object> driverSendDriversTripPush(
         @RequestParam(value = "fwd")String forward,
         @RequestBody Map<String, Object> driversTripPush);

   @PostMapping(path = "/api/trip_cancelled")
   ResponseEntity<Object> driverTripCancelled(
         @RequestParam(value = "fwd")String forward,
         @RequestParam(value = "driver_id") String driverId,
         @RequestBody Map<String, Object> tripCancelledBody);


   // -------------------------- CLIENT ---------------------------
   /**
    * Trip cancelled by driver/client/internal error
    */
   @PostMapping(path = "/api/services/back_to_client/trip_was_cancelled")
   ResponseEntity<Object> clientTripCancelled(
         @RequestParam(value = "fwd") String forward,
         @RequestBody Map<String, Object> tripCancelledBody);

   /**
    * Trip was accepted by the driver, notify the client service
    */
   @PostMapping(path = "/api/services/back_to_client/driver_found")
   ResponseEntity<Object> clientTripAccepted(
         @RequestParam(value = "fwd") String forward,
         @RequestBody Map<String, Object> tripAcceptedBody);

   /**
    * Driver completed the trip, notify the client
    */
   @PostMapping(path = "/api/services/back_to_client/trip_completed")
   ResponseEntity<Object> clientTripCompleted(
         @RequestParam(value = "fwd") String forward,
         @RequestBody Map<String, Object> tripCompletedBody);

   @PostMapping(path = "/api/services/back_to_client/trip_started")
   ResponseEntity<Object> clientTripStarted(
         @RequestParam(value = "fwd") String forward,
         @RequestBody Map<String, Object> tripStartedBody);
}
