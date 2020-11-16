package uber.trip_manager_service.services;

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
import uber.trip_manager_service.structures.external.SupplyInstance;
import uber.trip_manager_service.structures.internal.TripForDB;
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
   private final ConnectionDriver pendingTripsDB;

   @Autowired
   TripManagerImpl(final RestTemplate restTemplate,
                   final SupplyLocationClient supplyLocationClient,
                   final DbClient dbClient,
                   final ProxyClient proxyClient) {
      this.restTemplate = restTemplate;
      this.supplyLocationClient = supplyLocationClient;
      this.dbClient = dbClient;
      this.proxyClient = proxyClient;
      this.pendingTripsDB = new ConnectionDriver();
   }

   private <T> boolean requestFailed(ResponseEntity<T> resp) {
      return resp.getStatusCode() != HttpStatus.OK || resp.getBody() == null;
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
      UUID tripId = pendingTripsDB.addPendingTrip(
            tripRequestEntity.getClientId(),
            tripRequestEntity.getFrom(),
            tripRequestEntity.getTo());

      // send the message to the DriverService through proxy, update the proxy with requestState
      Map<String, Object> driversTripPush = Map.of("trip_id", tripId, "driver_ids", filteredSupply);

      return proxyClient.sendDriversTripPush(
            ServiceNames.Drivers.getLabel(), driversTripPush);
   }

   public ResponseEntity<Object> acceptTrip(UUID driverId, UUID tripId) {
      TripForDB trip = pendingTripsDB.getRemoveTrip(tripId);
      if (trip == null) {
         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      trip.setDriverId(driverId);
      trip.setStatus(TripForDB.TripStatus.ACCEPTED);

      // TODO: asynchronously send three messages to the client, driver, db
      // if any fails - revert the transaction, cancel the trip
      return new ResponseEntity<>(trip, HttpStatus.OK);
   }

   public ResponseEntity<Object> cancelTripClient(UUID clientId, UUID tripId) {
      // check pending first
      TripForDB trip = pendingTripsDB.getRemoveTrip(tripId);
      if (trip != null) {
         // calculate penalty for the client, but no driver involved
         return new ResponseEntity<>(HttpStatus.OK);
      }

      // proceed with ongoing trips
      var resp1 = dbClient.getRemoveTrip(tripId);
      if (requestFailed(resp1)) {
         return new ResponseEntity<>(resp1.getStatusCode());
      }

      trip = resp1.getBody();

      if (trip.getStatus() == TripForDB.TripStatus.ACCEPTED) {
         // calculate penalty for the client
         trip.setStatus(TripForDB.TripStatus.CANCELLED);

         // notify the driver the trip is cancelled
         var resp = proxyClient.tripCancelled(
               ServiceNames.Drivers.getLabel(),
               trip.getDriverId(),
               trip.getTripId());

         // trip is already deleted here
         return resp;
      } else if (trip.getStatus() == TripForDB.TripStatus.IN_PROGRESS) {

         // For the moment client can't cancel ongoing trip
         return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

      } else {

         return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
      }
   }

}
