package uber.trip_manager_service.services.driver;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import uber.trip_manager_service.clients.ClientsWrapper;
import uber.trip_manager_service.clients.DriversWrapper;
import uber.trip_manager_service.services.TripsStorageDriver;
import uber.trip_manager_service.structures.internal.TripForDB;

import java.util.concurrent.CompletableFuture;

@Component
public class RevertDriverHelperComponent {
   final ClientsWrapper clientsWrapper;
   final DriversWrapper driversWrapper;
   final TripsStorageDriver tripsStorage;

   RevertDriverHelperComponent(
         final ClientsWrapper clientsWrapper,
         final DriversWrapper driversWrapper,
         final TripsStorageDriver tripsStorage) {
      this.clientsWrapper = clientsWrapper;
      this.driversWrapper = driversWrapper;
      this.tripsStorage = tripsStorage;
   }

   private void cancelDriverRemove(TripForDB trip) {
      tripsStorage.getRemovePending(trip.getTripId());
      CompletableFuture.runAsync(()->
            driversWrapper.tripCancelled(
                  trip.getDriverId(),
                  trip.getTripId())
      );
   }

   private void cancelClientRemove(TripForDB trip) {
      tripsStorage.getRemovePending(trip.getTripId());
      CompletableFuture.runAsync(()->
            clientsWrapper.tripCancelled(
                  trip.getClientId(),
                  trip.getTripId())
      );
   }

   public void tripAccepted(TripForDB trip,
                            CompletableFuture<ResponseEntity<Object>> dbFuture,
                            CompletableFuture<ResponseEntity<Object>> clientFuture) {
      /* TODO: Uncomment once driver service is available
      ResponseEntity<Object> resp;
      // If updating driver Status in the DB failed,
      // we try to cancel the trip then.
      try {
         resp = dbFuture.get();

         if (resp.getStatusCode() != HttpStatus.OK) {
            cancelDriverRemove(trip);
         }
      } catch (Exception ex) {
         cancelDriverRemove(trip);
      }


      // If sending Trip to the client failed,
      // we try to cancel the trip then.
      try {
         resp = clientFuture.get();

         if (resp.getStatusCode() != HttpStatus.OK) {
            cancelClientRemove(trip);
         }
      } catch (Exception ex) {
         cancelClientRemove(trip);
      }
      */
   }
}
