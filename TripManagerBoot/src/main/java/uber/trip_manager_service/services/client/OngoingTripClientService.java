package uber.trip_manager_service.services.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import uber.trip_manager_service.clients.ClientsWrapper;
import uber.trip_manager_service.clients.DbClient;
import uber.trip_manager_service.clients.DriversWrapper;
import uber.trip_manager_service.services.TripsStorageDriver;
import uber.trip_manager_service.structures.internal.TripForDB;
import uber.trip_manager_service.structures.internal.TripForDriver;
import uber.trip_manager_service.utils.ServiceNames;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class OngoingTripClientService {
   private final DbClient dbClient;
   private final DriversWrapper driversWrapper;
   private final TripsStorageDriver tripsStorage;
   private final RevertClientHelperComponent revertClientHelper;

   private ResponseEntity<Object> resp;
   @Autowired
   OngoingTripClientService(final DbClient dbClient,
                            final DriversWrapper driversWrapper,
                            final TripsStorageDriver tripsStorage,
                            final RevertClientHelperComponent revertClientHelper) {
      this.dbClient = dbClient;
      this.driversWrapper = driversWrapper;
      this.tripsStorage = tripsStorage;
      this.revertClientHelper = revertClientHelper;
   }


   public void cancelTrip(
         DeferredResult<ResponseEntity<Object>> output,
         UUID clientId, UUID tripId) {
      // check pending first
      final TripForDB pendingTrip = tripsStorage.getPending(tripId);
      if (pendingTrip != null) {
         if (!pendingTrip.getClientId().equals(clientId)) {
            output.setResult(new ResponseEntity<>(
                  "Found same requested trip, but with different rider",
                  HttpStatus.PRECONDITION_FAILED));
         } else {
            // calculate penalty for the client, but no driver involved
            tripsStorage.getRemovePending(tripId);
            output.setResult(new ResponseEntity<>(HttpStatus.OK));
         }
         return;
      }

      // proceed with ongoing trips
      final TripForDB trip = tripsStorage.getOngoing(tripId);
      if (trip == null) {
         output.setResult(new ResponseEntity<>(
               "Unable to cancel - trip not found",
               HttpStatus.PRECONDITION_FAILED));

      } else if (!trip.getClientId().equals(clientId)) {
         output.setResult(new ResponseEntity<>(
               "Found same ongoing trip, but with different rider",
               HttpStatus.PRECONDITION_FAILED));

      } else if (trip.getStatus() != TripForDB.TripStatus.ACCEPTED) {
         output.setResult(new ResponseEntity<>(
               "Driver can't cancel running trip",
               HttpStatus.PRECONDITION_FAILED));

      } else {
         tripsStorage.getRemoveOngoing(tripId);
         // calculate penalty for the client
         trip.setCancelled();

         CompletableFuture<ResponseEntity<Object>> dbUpdateFuture =
               CompletableFuture.supplyAsync(
                     () -> dbClient.updateDriverStatus(
                           trip.getDriverId(),
                           false)
         );

         // notify the driver the trip is cancelled
         try {
            resp = driversWrapper.tripCancelled(
                  trip.getDriverId(),
                  trip.getTripId());
         } catch (Exception ex) {
            output.setResult(new ResponseEntity<>(HttpStatus.OK));
            revertClientHelper.tripCancelled(trip, null, dbUpdateFuture);
            return;
         }
         revertClientHelper.tripCancelled(trip, resp, dbUpdateFuture);

         // trip is already deleted here
         output.setResult(new ResponseEntity<>(HttpStatus.OK));
      }
   }
}
