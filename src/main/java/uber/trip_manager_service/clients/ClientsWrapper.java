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
         String clientId, UUID tripId, String driverId) {

      return proxyClient.clientTripAccepted(
            ServiceNames.Clients.getLabel(),
            Map.of(
                  "rider_id", clientId,
                  "trip_id", tripId,
                  "driver_id", driverId,
                  "payload", "Dummy payload"
            )
      );
   }

   public ResponseEntity<Object> tripCancelled(
         String clientId, UUID tripId) {

      return proxyClient.clientTripCancelled(
            ServiceNames.Clients.getLabel(),
            Map.of(
                  "rider_id", clientId,
                  "trip_id", tripId,
                  "reason", "Dummy reason"
            )
      );
   }

   public ResponseEntity<Object> tripCompleted(
         String clientId, UUID tripId) {

      return proxyClient.clientTripCompleted(
            ServiceNames.Clients.getLabel(),
            Map.of(
                  "rider_id", clientId,
                  "trip_id", tripId,
                  "payload", ""
            )
      );
   }

   public ResponseEntity<Object> tripStarted(
         String clientId, UUID tripId) {
      return proxyClient.clientTripStarted(
            ServiceNames.Clients.getLabel(),
            Map.of(
                  "rider_id", clientId,
                  "trip_id", tripId
            )
      );
   }
}
