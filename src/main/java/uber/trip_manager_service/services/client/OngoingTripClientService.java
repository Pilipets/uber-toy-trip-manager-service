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
import uber.trip_manager_service.structures.external.GeoPoint;
import uber.trip_manager_service.structures.internal.TripForDB;
import uber.trip_manager_service.utils.HttpUtils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class OngoingTripClientService {
   private final DbClient dbClient;
   private final DriversWrapper driversWrapper;
   private final TripsStorageDriver tripsStorage;
   private final RevertClientHelperComponent revertHelper;
   private final SupplyLocationClient supplyClient;

   @Autowired
   OngoingTripClientService(
         final DbClient dbClient,
         final DriversWrapper driversWrapper,
         final TripsStorageDriver tripsStorage,
         final RevertClientHelperComponent revertHelper,
         final SupplyLocationClient supplyClient) {
      this.dbClient = dbClient;
      this.driversWrapper = driversWrapper;
      this.tripsStorage = tripsStorage;
      this.revertHelper = revertHelper;
      this.supplyClient = supplyClient;
   }


   public void cancelTrip(
         DeferredResult<ResponseEntity<Object>> output,
         String clientId, UUID tripId) {
      // check pending first
      final TripForDB pendingTrip = tripsStorage.getPending(tripId);
      if (pendingTrip != null) {
         // calculate penalty for the client, but no driver involved
         tripsStorage.getRemovePending(tripId);
         output.setResult(new ResponseEntity<>(HttpStatus.OK));
         return;
      }

      // proceed with ongoing trips
      final TripForDB trip = tripsStorage.getOngoing(tripId);
      if (trip == null) {
         output.setResult(new ResponseEntity<>(
               "Unable to cancel - trip not found",
               HttpStatus.PRECONDITION_FAILED));

      } else if (trip.getStatus() != TripForDB.TripStatus.ACCEPTED) {
         output.setResult(new ResponseEntity<>(
               "Rider can't cancel running trip",
               HttpStatus.PRECONDITION_FAILED));

      } else {
         tripsStorage.getRemoveOngoing(tripId);
         // calculate penalty for the client
         trip.setCancelled();

         CompletableFuture<ResponseEntity<Object>> dbUpdateFuture =
               CompletableFuture.supplyAsync(
                     () -> dbClient.updateDriverBody(
                           trip.getDriverId(),
                           Map.of("on_the_ride", false)
                     )
               );

         CompletableFuture<ResponseEntity<Object>> driverCancelFuture =
               CompletableFuture.supplyAsync(
                     ()-> driversWrapper.tripCancelled(
                           trip.getDriverId(),
                           tripId)
               );

         if (revertHelper.tripCancelled(trip, driverCancelFuture, dbUpdateFuture)) {
            output.setResult(new ResponseEntity<>(HttpStatus.OK));
         } else {
            output.setResult(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
         }
      }
   }

   public void getDriverLocation(DeferredResult<ResponseEntity<GeoPoint>> output, UUID tripId) {
      final TripForDB trip = tripsStorage.getOngoing(tripId);
      if (trip == null || trip.getStatus() != TripForDB.TripStatus.ACCEPTED) {
         output.setResult(new ResponseEntity<>(
               HttpStatus.PRECONDITION_FAILED));
      } else {
         boolean failed;
         ResponseEntity<GeoPoint> resp = null;
         try {
            resp = supplyClient.getSupplyLocation(trip.getDriverId());
            failed = !HttpUtils.isValidResponse(resp) || resp.getBody() == null;
         } catch (Exception ex) {
            failed = true;
         }

         if (failed) {
            output.setResult(new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY));
         } else {
            output.setResult(new ResponseEntity<>(resp.getBody(), HttpStatus.OK));
         }
      }
   }
}
