package uber.trip_manager_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "proxy-service",
      url = "http://localhost:8078/proxy_service")
public interface ProxyClient {
   @PostMapping(path="/send_trip")
   ResponseEntity sendDriversTripPush(
         @RequestParam(value = "to")String forward,
         @RequestBody Map<String, Object> driversTripPush);

   @PostMapping(path="/temp")
   void testSend(@RequestBody List<String> text);
}
