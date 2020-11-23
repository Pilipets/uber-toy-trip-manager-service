package uber.trip_manager_service.services.client;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import uber.trip_manager_service.clients.ClientsWrapper;
import uber.trip_manager_service.services.TripsStorageDriver;
import uber.trip_manager_service.structures.internal.TripForDB;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
public class RevertClientHelperComponent {
   final TripsStorageDriver tripsStorage;
   final ClientsWrapper clientsWrapper;

   public RevertClientHelperComponent(
         final TripsStorageDriver tripsStorage,
         final ClientsWrapper clientsWrapper) {
      this.tripsStorage = tripsStorage;
      this.clientsWrapper = clientsWrapper;
   }

   @Async
   public void tripRequested(
         TripForDB trip,
         CompletableFuture<ResponseEntity<Object>> driverFuture) {
      /* TODO: Uncomment once db is available
      ResponseEntity<Object> resp = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      boolean notifyFailed = false;
      try {
         resp = driverFuture.get();
      } catch (Exception ex) {
         notifyFailed = true;
      }
      notifyFailed = notifyFailed || (resp.getStatusCode() != HttpStatus.OK);
      if (!notifyFailed) {
         return;
      }

      // Notify client about the trip error
      tripsStorage.getRemovePending(trip.getTripId());
      CompletableFuture.runAsync(()->
            clientsWrapper.tripCancelled(
                  trip.getClientId(),
                  trip.getTripId())
      );
      */
   }
}
