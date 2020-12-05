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
import uber.trip_manager_service.structures.internal.TripForDriver;

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
         UUID driverId, UUID tripId) {

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
                           driverId,
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

   public void startTrip(
         DeferredResult<ResponseEntity<TripForDriver>> output,
         UUID driverId, UUID tripId) {
      final TripForDB trip = tripsStorage.getOngoing(tripId);

      if (trip == null ||
            !trip.getDriverId().equals(driverId) ||
            trip.getStatus() != TripForDB.TripStatus.ACCEPTED) {
         output.setResult(new ResponseEntity<>(
               HttpStatus.PRECONDITION_FAILED));
      }

      ResponseEntity<Object> resp;
      try {
         resp = clientsWrapper.tripStarted(trip.getClientId(), tripId);
      } catch (Exception ex) {
         output.setResult(new ResponseEntity<>(
               HttpStatus.INTERNAL_SERVER_ERROR)
         );
         return;
      }

      if (resp.getStatusCode() != HttpStatus.OK) {
         output.setResult(new ResponseEntity<>(
               resp.getStatusCode()
         ));
      } else {

         trip.setStarted();
         TripForDriver tripForDriver = new TripForDriver(trip.getClientId(), tripId, trip.getToPoint());
         output.setResult(new ResponseEntity<>(
               tripForDriver,
               HttpStatus.OK)
         );
      }
   }

   public void completeTrip(
         DeferredResult<ResponseEntity<Object>> output,
         UUID driverId, UUID tripId) {
      final TripForDB trip = tripsStorage.getOngoing(tripId);

      if (trip == null ||
            !trip.getDriverId().equals(driverId) ||
            trip.getStatus() != TripForDB.TripStatus.IN_PROGRESS) {
         output.setResult(new ResponseEntity<>(
               HttpStatus.PRECONDITION_FAILED));
         return;
      }

      ResponseEntity<Object> resp;
      try {
         resp = clientsWrapper.tripCompleted(trip.getClientId(), tripId);
      } catch (Exception ex) {
         output.setResult(new ResponseEntity<>(
               HttpStatus.INTERNAL_SERVER_ERROR
         ));
         return;
      }

      if (resp.getStatusCode() != HttpStatus.OK) {
         output.setResult(new ResponseEntity<>(
               resp.getStatusCode()
         ));
      } else {

         trip.setCompleted();
         // do Price calculation here

         resp = null;
         try {
            resp = clientsWrapper.tripCompleted(trip.getClientId(), tripId);
         } catch (Exception ex) {
            output.setResult(
                  new ResponseEntity<>(
                     ex.getMessage(),
                     HttpStatus.FAILED_DEPENDENCY)
            );
            return;
         }

         if (resp.getStatusCode() != HttpStatus.OK) {
            output.setResult(new ResponseEntity<>(resp.getStatusCode()));
            return;
         }

         tripsStorage.getRemoveOngoing(tripId);
         CompletableFuture<ResponseEntity<Object>> dbTripUpdate =
               CompletableFuture.supplyAsync(
                     ()->dbClient.saveTrip(trip)
               );

         CompletableFuture<ResponseEntity<Object>> dbStatusUpdate =
               CompletableFuture.supplyAsync(
                     ()->dbClient.updateDriverStatus(driverId, false)
               );

         output.setResult(new ResponseEntity<>(HttpStatus.OK));

         revertHelper.tripCompleted(trip, dbStatusUpdate, dbTripUpdate);
      }
   }
}