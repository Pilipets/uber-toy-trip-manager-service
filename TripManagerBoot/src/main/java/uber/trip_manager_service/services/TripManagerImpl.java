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
import uber.trip_manager_service.structures.FilterTripParams;
import uber.trip_manager_service.structures.GeoPoint;
import uber.trip_manager_service.structures.SupplyReturnType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Filter;

@Service
public class TripManagerImpl {
   private final RestTemplate restTemplate;
   private final ObjectMapper jsonMapper = new ObjectMapper();
   private final SupplyLocationClient supplyLocationClient;
   private final DbClient dbClient;
   private final ProxyClient proxyClient;

   @Autowired
   TripManagerImpl(final RestTemplate restTemplate,
                   final SupplyLocationClient supplyLocationClient,
                   final DbClient dbClient,
                   final ProxyClient proxyClient) {
      this.restTemplate = restTemplate;
      this.supplyLocationClient = supplyLocationClient;
      this.dbClient = dbClient;
      this.proxyClient = proxyClient;
   }

   public ResponseEntity<Object> newTrip(
         UUID clientUUID,
         FilterTripParams params,
         GeoPoint where,
         GeoPoint to) throws JsonProcessingException {
      // String uri = "http://localhost:8080/supply_location_service/get_closest?geoPoint={argJson}";
      String argJson = jsonMapper.writeValueAsString(where);

      List<SupplyReturnType> res = supplyLocationClient.getClosestSupply(argJson);
      //restTemplate.getForObject(uri, List.class, argJson);

      if (res == null || res.size() == 0) {
         return new ResponseEntity<>(null, HttpStatus.OK);
      }

      List<UUID> filteredSupply = new ArrayList<>(res.size());
      for (SupplyReturnType ins : res) {
         filteredSupply.add(ins.getSupplyInstance().getUUID());
      }

      // perform call to the DB service to filter the points by given criteria
      filteredSupply = dbClient.filterSupply(params, filteredSupply);
      if (filteredSupply == null || filteredSupply.size() == 0) {
         return new ResponseEntity<>(null, HttpStatus.OK);
      }

      // send the message to the DriverService through proxy, update the proxy with requestState
      // proxyClient.sendNotificationsToSupply(clientUUID, filteredSupply);

      return new ResponseEntity<>(null, HttpStatus.OK);
   }

   public void acceptTrip(UUID driver_uuid, UUID tripUUID) {

   }

   public void cancelTrip(UUID uuid, UUID tripUUID) {

   }

   public void completeTrip(UUID driverUUID, UUID tripUUID) {

   }
}
