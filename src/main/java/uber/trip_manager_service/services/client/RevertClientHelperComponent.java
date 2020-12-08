package uber.trip_manager_service.services.client;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import uber.trip_manager_service.clients.ClientsWrapper;
import uber.trip_manager_service.services.TripsStorageDriver;
import uber.trip_manager_service.structures.internal.TripForDB;
import uber.trip_manager_service.utils.HttpUtils;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class RevertClientHelperComponent {
   final TripsStorageDriver tripsStorage;
   final ClientsWrapper clientsWrapper;
   final Logger logger = Logger.getLogger(RevertClientHelperComponent.class.getName());

   public RevertClientHelperComponent(
         final TripsStorageDriver tripsStorage,
         final ClientsWrapper clientsWrapper) {
      this.tripsStorage = tripsStorage;
      this.clientsWrapper = clientsWrapper;
   }

   public void tripRequested(
         TripForDB trip,
         CompletableFuture<ResponseEntity<Object>> driverFuture) {
      ///* TODO: Uncomment once driver is available
      boolean requestFailed = false;
      ResponseEntity<Object> resp = null;
      try {
         resp = driverFuture.get();
      } catch (Exception ex) {
         logger.log(Level.INFO, ex.getMessage());
         requestFailed = true;
      }

      if (!requestFailed && !HttpUtils.isValidResponse(resp)) {
         logger.log(Level.INFO, String.format(
               "Unable to update driver status, received %d", resp.getStatusCode()));
         requestFailed = true;
      }

      /*
      if (requestFailed) {
         // Notify client about the trip error
         tripsStorage.getRemovePending(trip.getTripId());
         CompletableFuture.runAsync(() ->
               clientsWrapper.tripCancelled(
                     trip.getClientId(),
                     trip.getTripId())
         );
      }*/
   }

   public boolean tripCancelled(
         TripForDB trip,
         CompletableFuture<ResponseEntity<Object>> driverCancelFuture,
         CompletableFuture<ResponseEntity<Object>> dbUpdateFuture) {
      // Firstly, trip is already deleted

      ResponseEntity<Object> resp = null;
      try {
         resp = driverCancelFuture.get();
      } catch (Exception ex) {
        logger.log(Level.WARNING, ex.getMessage());
      }
      if (!HttpUtils.isValidResponse(resp)) {
         logger.log(Level.INFO, String.format(
               "Unable to update driver status, received %d", resp.getStatusCode()));
         // Some problem with cancelling on the driver
      }

      resp = null;
      try {
         resp = dbUpdateFuture.get();
      } catch (Exception ex) {
         logger.log(Level.WARNING, ex.getMessage());
      }
      if (!HttpUtils.isValidResponse(resp)) {
         logger.log(Level.INFO, String.format(
               "Unable to update db status, received %d", resp.getStatusCode()));
         // Some problem with updating the db driver
      }
      return true;
   }
}
