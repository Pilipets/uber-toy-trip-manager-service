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

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class BeforeTripDriverService {
   private final SupplyLocationClient supplyLocationClient;
   private final DbClient dbClient;
   private final ClientsWrapper clientsWrapper;
   private final TripsStorageDriver tripsStorage;
   private final RevertDriverHelperComponent revertHelper;

   private ResponseEntity<Object> resp = null;

   @Autowired
   BeforeTripDriverService(
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

   private <T> boolean requestFailed(ResponseEntity<List<T>> resp) {
      return ((resp.getStatusCode() != HttpStatus.OK)
            || resp.getBody() == null
            || resp.getBody().isEmpty());
   }

   public void acceptTrip(
         DeferredResult<ResponseEntity<TripForDriver>> output,
         UUID driverId, UUID tripId) {

      TripForDB trip = tripsStorage.getRemovePending(tripId);
      if (trip == null) {
         output.setResult(new ResponseEntity<>(
               HttpStatus.PRECONDITION_FAILED));
         return;
      }

      trip.setAccepted(driverId);

      // update the driverStatus in the DB
      CompletableFuture<ResponseEntity<Object>> f1 = CompletableFuture.supplyAsync(
            ()->dbClient.updateDriverStatus(driverId, true)
      );

      // notify client about the trip accepted by Driver
      CompletableFuture<ResponseEntity<Object>> f2 = CompletableFuture.supplyAsync(
            () -> clientsWrapper.tripAccepted(
                  trip.getClientId(),
                  tripId,
                  trip.getDriverId())
      );

      // save trip as ongoing
      tripsStorage.addOngoingTrip(trip);

      // send trip to the Driver
      TripForDriver respTrip = new TripForDriver(driverId, tripId, trip.getFromPoint());
      output.setResult(new ResponseEntity<>(respTrip, HttpStatus.OK));

      revertHelper.tripAccepted(trip, f1, f2);
   }
}
