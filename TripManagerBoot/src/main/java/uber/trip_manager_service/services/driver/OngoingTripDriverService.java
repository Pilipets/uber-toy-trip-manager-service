package uber.trip_manager_service.services.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import uber.trip_manager_service.clients.ClientsWrapper;
import uber.trip_manager_service.clients.DbClient;
import uber.trip_manager_service.clients.SupplyLocationClient;
import uber.trip_manager_service.services.TripsStorageDriver;
import uber.trip_manager_service.structures.internal.TripForDB;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class OngoingTripDriverService {
   private final SupplyLocationClient supplyLocationClient;
   private final DbClient dbClient;
   private final ClientsWrapper clientsWrapper;
   private final TripsStorageDriver tripsStorage;
   private final RevertDriverHelperComponent revertHelper;

   @Autowired
   OngoingTripDriverService(
         final SupplyLocationClient supplyLocationClient,
         final DbClient dbClient,
         final ClientsWrapper clientsWrapper,
         final TripsStorageDriver tripsStorage,
         final RevertDriverHelperComponent revertHelper) {
      this.supplyLocationClient = supplyLocationClient;
      this.dbClient = dbClient;
      this.clientsWrapper = clientsWrapper;
      this.tripsStorage = tripsStorage;
      this.revertHelper = revertHelper;
   }

   public void cancelTrip(
         DeferredResult<ResponseEntity<Object>> output,
         UUID driverId,
         UUID tripId) {

      final TripForDB trip = tripsStorage.getOngoing(tripId);
      if (trip == null) {
         output.setResult(new ResponseEntity<>(
               "Unable to cancel - trip not found",
               HttpStatus.PRECONDITION_FAILED));

      } else if (!trip.getDriverId().equals(driverId)) {
         output.setResult(new ResponseEntity<>(
               "Found same ongoing trip, but with different driver",
               HttpStatus.PRECONDITION_FAILED));

      } else if (trip.getStatus() != TripForDB.TripStatus.ACCEPTED) {
         output.setResult(new ResponseEntity<>(
               "Driver can't cancel running trip",
               HttpStatus.PRECONDITION_FAILED));
      } else {
         tripsStorage.getRemoveOngoing(tripId);

         CompletableFuture<ResponseEntity<Object>> dbUpdateFuture =
               CompletableFuture.supplyAsync(
                     () -> dbClient.updateDriverStatus(
                           trip.getDriverId(),
                           false)
               );

         CompletableFuture<ResponseEntity<Object>> clientCancelFuture =
               CompletableFuture.supplyAsync(
                     ()-> clientsWrapper.tripCancelled(
                           trip.getClientId(),
                           tripId)
               );

         if (revertHelper.tripCancelled(trip, clientCancelFuture, dbUpdateFuture)) {
            output.setResult(new ResponseEntity<>(HttpStatus.OK));
         } else {
            output.setResult(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
         }
      }
   }
}