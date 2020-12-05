package uber.trip_manager_service.clients;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uber.trip_manager_service.utils.ServiceNames;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class DriversWrapper {
   final ProxyClient proxyClient;

   DriversWrapper(
         final ProxyClient proxyClient) {
      this.proxyClient = proxyClient;
   }

   public ResponseEntity<Object> tripCancelled(
         UUID driverId, UUID tripId) {

      return proxyClient.driverTripCancelled(
            ServiceNames.Drivers.getLabel(),
            driverId,
            Map.of("trip_id", tripId)
      );
   }

   public ResponseEntity<Object> sendDriversTripPush(
         UUID tripId, List<UUID> driverIds) {
      return proxyClient.driverSendDriversTripPush(
            ServiceNames.Drivers.getLabel(),
            Map.of(
                  "trip_id", tripId,
                  "driver_ids", driverIds
            )
      );
   }
}
