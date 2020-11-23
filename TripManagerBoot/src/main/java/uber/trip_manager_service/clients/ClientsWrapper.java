package uber.trip_manager_service.clients;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uber.trip_manager_service.utils.ServiceNames;

import java.util.Map;
import java.util.UUID;

@Service
public class ClientsWrapper {
   final ProxyClient proxyClient;

   ClientsWrapper(final ProxyClient proxyClient) {
      this.proxyClient = proxyClient;
   }

   public ResponseEntity<Object> tripAccepted(
         UUID clientId, UUID tripId, UUID driverId) {

      return proxyClient.tripAccepted(
            ServiceNames.Clients.getLabel(),
            clientId,
            Map.of(
                  "trip_id", tripId,
                  "driver_id", driverId
            )
      );
   }

   public ResponseEntity<Object> tripCancelled(
         UUID clientId, UUID tripId) {

      return proxyClient.tripCancelled(
            ServiceNames.Clients.getLabel(),
            clientId,
            tripId
      );
   }
}
