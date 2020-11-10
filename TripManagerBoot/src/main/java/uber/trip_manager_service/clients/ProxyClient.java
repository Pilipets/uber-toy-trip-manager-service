package uber.trip_manager_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "proxy-service",
      url = "http://localhost:8084/proxy_service")
public interface ProxyClient {
   @PostMapping
   void sendNotificationsToSupply(
         @RequestParam(value = "client_uuid")UUID uuid,
         List<UUID> supplyUUID);
}
