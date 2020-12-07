package uber.trip_manager_service.services.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import uber.trip_manager_service.clients.DbClient;
import uber.trip_manager_service.clients.DriversWrapper;
import uber.trip_manager_service.clients.SupplyLocationClient;
import uber.trip_manager_service.services.TripsStorageDriver;
import uber.trip_manager_service.structures.external.SupplyInstance;
import uber.trip_manager_service.structures.internal.FilterTripParams;
import uber.trip_manager_service.structures.internal.TripForDB;
import uber.trip_manager_service.structures.internal.TripRequestEntity;
import uber.trip_manager_service.utils.HttpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class BeforeTripClientService {
   private final SupplyLocationClient supplyLocationClient;
   private final DbClient dbClient;
   private final DriversWrapper driversWrapper;
   private final TripsStorageDriver tripsStorage;
   private final RevertClientHelperComponent revertClientHelper;

   @Autowired
   BeforeTripClientService(
         final SupplyLocationClient supplyLocationClient,
         final DbClient dbClient,
         final DriversWrapper driversWrapper,
         final TripsStorageDriver tripsStorage,
         final RevertClientHelperComponent revertClientHelper) {

      this.supplyLocationClient = supplyLocationClient;
      this.dbClient = dbClient;
      this.driversWrapper = driversWrapper;
      this.tripsStorage = tripsStorage;
      this.revertClientHelper = revertClientHelper;
   }

   private <T> boolean requestFailed(ResponseEntity<List<T>> resp) {
      return !HttpUtils.isValidResponse(resp) ||
            resp.getBody() == null ||
            resp.getBody().isEmpty();
   }

   public void requestNewTrip(
         DeferredResult<ResponseEntity<Object>> output,
         TripRequestEntity tripRequestEntity) {
      // get closest supply to given pick-up location
      ResponseEntity<List<SupplyInstance>> resp1;
      try {
         resp1 = supplyLocationClient.getClosestSupply(
               tripRequestEntity.getFromPoint().getLocation());
      } catch (Exception ex) {
         output.setResult(new ResponseEntity<>(
               Map.of("ex", ex.getMessage(),
                     "description", "Unable to find closest drivers"),
               HttpStatus.FAILED_DEPENDENCY));
         return;
      }

      if (requestFailed(resp1)) {
         output.setResult(new ResponseEntity<>(
               "Unable to find closest drivers",
               HttpStatus.FAILED_DEPENDENCY));
         return;
      }

      // obtain ids from given supply
      List<String> supplyIds = new ArrayList<>(resp1.getBody().size());
      for (SupplyInstance ins : resp1.getBody()) {
         supplyIds.add(ins.getId());
      }

      /* TODO: Uncomment once db is available
      // perform call to the DB service to filter the points by given criteria
      FilterTripParams params = new FilterTripParams(tripRequestEntity.getParams());
      ResponseEntity<List<String>> resp2;
      try {
         resp2 = dbClient.filterSupply(params, supplyIds);
      } catch (Exception ex) {
         output.setResult(new ResponseEntity<>(
               Map.of("ex", ex.getMessage(),
                     "description", "Unable to filter drivers by request params"),
               HttpStatus.FAILED_DEPENDENCY));
         return;
      }

      if (requestFailed(resp2)) {
         output.setResult(new ResponseEntity<>(
               "Unable to filter drivers by request params",
               HttpStatus.FAILED_DEPENDENCY));
         return;
      } */

      final List<String> filteredSupply = supplyIds; //resp2.getBody();

      // store hanging trip in the DB
      TripForDB trip = tripsStorage.addPendingTrip(
            tripRequestEntity.getClientId(),
            tripRequestEntity.getFromPoint(),
            tripRequestEntity.getToPoint());

      // we know that there are drivers at this stage
      output.setResult(ResponseEntity.ok(trip.getTripId()));


      // send the message to the DriverService through proxy
      CompletableFuture<ResponseEntity<Object>> driverFuture =
            CompletableFuture.supplyAsync(()->
                  driversWrapper.sendDriversTripPush(
                        trip.getTripId(),
                        filteredSupply
                  )
            );

      revertClientHelper.tripRequested(trip, driverFuture);
   }
}
