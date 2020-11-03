package uber.trip_manager_service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uber.trip_manager_service.structures.GeoPoint;
import uber.trip_manager_service.structures.SupplyInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TripManagerImpl {
   private final RestTemplate restTemplate;
   private final ObjectMapper jsonMapper = new ObjectMapper();

   @Autowired
   TripManagerImpl(final RestTemplate restTemplate) {
      this.restTemplate = restTemplate;
   }

   public void newTrip(
         UUID clientUUID, GeoPoint where, GeoPoint to) throws JsonProcessingException {
      String uri = "http://localhost:8080/supply_location_service/get_closest?geoPoint={argJson}";
      String argJson = jsonMapper.writeValueAsString(where);


      //List<Pair<SupplyInstance, Double>> points = impl.getClosestPoints(where);
      List<Pair<SupplyInstance, Double>> res =
            restTemplate.getForObject(uri, List.class, argJson);


      if (res == null || res.size() == 0) {
         return;
      }

      List<UUID> filterResult = new ArrayList<>(res.size());
      // perform call to the DB service to filter the points by given criteria

      // add to the internal DB pendingTrip

      // send to the proxy info that trip pending
   }

   public void acceptTrip(UUID driver_uuid, UUID tripUUID) {

   }

   public void cancelTrip(UUID uuid, UUID tripUUID) {

   }

   public void completeTrip(UUID driverUUID, UUID tripUUID) {

   }
}
