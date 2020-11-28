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

      return proxyClient.clientTripAccepted(
            ServiceNames.Clients.getLabel(),
            Map.of(
                  "rider_id", clientId,
                  "trip_id", tripId,
                  "driver_id", driverId,
                  "payload", null
            )
      );
   }

   public ResponseEntity<Object> tripCancelled(
         UUID clientId, UUID tripId) {

      return proxyClient.clientTripCancelled(
            ServiceNames.Clients.getLabel(),
            Map.of(
                  "rider_id", clientId,
                  "trip_id", tripId,
                  "reason", null
            )
      );
   }

   public ResponseEntity<Object> tripCompleted(
         UUID clientId, UUID tripId) {

      return proxyClient.clienTripCompleted(
            ServiceNames.Clients.getLabel(),
            Map.of(
                  "rider_id", clientId,
                  "trip_id", tripId,
                  "payload", null
            )
      );
   }
}
