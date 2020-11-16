package uber.trip_manager_service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uber.trip_manager_service.clients.DbClient;
import uber.trip_manager_service.clients.ProxyClient;
import uber.trip_manager_service.clients.SupplyLocationClient;
import uber.trip_manager_service.structures.internal.FilterTripParams;
import uber.trip_manager_service.structures.external.GeoPoint;
import uber.trip_manager_service.structures.external.SupplyInstance;
import uber.trip_manager_service.structures.internal.TripRequestEntity;
import uber.trip_manager_service.utils.ConnectionDriver;
import uber.trip_manager_service.utils.ServiceNames;

import java.util.*;

@Service
public class TripManagerImpl {
   private final RestTemplate restTemplate;
   private final ObjectMapper jsonMapper = new ObjectMapper();
   private final SupplyLocationClient supplyLocationClient;
   private final DbClient dbClient;
   private final ProxyClient proxyClient;
   private ConnectionDriver db;

   @Autowired
   TripManagerImpl(final RestTemplate restTemplate,
                   final SupplyLocationClient supplyLocationClient,
                   final DbClient dbClient,
                   final ProxyClient proxyClient) {
      this.restTemplate = restTemplate;
      this.supplyLocationClient = supplyLocationClient;
      this.dbClient = dbClient;
      this.proxyClient = proxyClient;
      this.db = new ConnectionDriver();
   }

   private <T> boolean requestFailed(ResponseEntity<List<T>> resp) {
      return resp.getStatusCode() != HttpStatus.OK
            || resp.getBody() == null || resp.getBody().isEmpty();
   }

   public ResponseEntity<Object> newTripRequest(TripRequestEntity tripRequestEntity) {
      // get closest supply to given pick-up location
      var resp1 = supplyLocationClient.getClosestSupply(tripRequestEntity.getFrom());
      if (requestFailed(resp1)) {
         return new ResponseEntity(resp1.getStatusCode());
      }

      List<SupplyInstance> closestSupply = resp1.getBody();

      // obtain ids from given supply
      List<UUID> filteredSupply = new ArrayList<>(closestSupply.size());
      for (SupplyInstance ins : closestSupply) {
         filteredSupply.add(ins.getId());
      }

      // perform call to the DB service to filter the points by given criteria
      FilterTripParams params = new FilterTripParams(tripRequestEntity.getParams());
      var resp2 = dbClient.filterSupply(params, filteredSupply);
      if (requestFailed(resp2)) {
         return new ResponseEntity(resp2.getStatusCode());
      }
      filteredSupply = resp2.getBody();

      // store hanging trip in the DB
      UUID tripId = db.addPendingTrip(
            tripRequestEntity.getClientId(),
            tripRequestEntity.getFrom(),
            tripRequestEntity.getTo());

      // send the message to the DriverService through proxy, update the proxy with requestState
      Map<String, Object> driversTripPush = Map.of("trip_id", tripId, "driver_ids", filteredSupply);

      return proxyClient.sendDriversTripPush(
            ServiceNames.Drivers.getLabel(), driversTripPush);
   }

   public void acceptTrip(UUID driver_uuid, UUID tripUUID) {

   }

      /*
   public void cancelTrip(UUID uuid, UUID tripUUID) {

   }

   public void completeTrip(UUID driverUUID, UUID tripUUID) {

   }
*/
}
