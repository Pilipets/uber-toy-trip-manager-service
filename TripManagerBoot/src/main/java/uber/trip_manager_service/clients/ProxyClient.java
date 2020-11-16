package uber.trip_manager_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "proxy-service", url = "http://localhost:8078/proxy_service")
public interface ProxyClient {
   @PostMapping(path="/send_trip")
   ResponseEntity<Object> sendDriversTripPush(
         @RequestParam(value = "forward")String forward,
         @RequestBody Map<String, Object> driversTripPush);

   @PostMapping(path = "/trip_cancelled")
   ResponseEntity<Object> tripCancelled(
         @RequestParam(value = "forward")String forward,
         @RequestParam(value = "id") UUID id,
         @RequestParam(value = "trip_id") UUID tripId);
}
