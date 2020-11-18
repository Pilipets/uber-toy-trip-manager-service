package uber.trip_manager_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uber.trip_manager_service.clients.DbClient;
import uber.trip_manager_service.clients.ProxyClient;
import uber.trip_manager_service.structures.internal.TripForDB;
import uber.trip_manager_service.structures.internal.TripForDriver;
import uber.trip_manager_service.utils.ServiceNames;

import java.util.UUID;

@Service
public class OngoingTripService {
   private final DbClient dbClient;
   private final ProxyClient proxyClient;
   private final TripsStorageDriver tripsStorage;

   @Autowired
   OngoingTripService(final DbClient dbClient,
                      final ProxyClient proxyClient,
                      final TripsStorageDriver tripsStorage) {
      this.dbClient = dbClient;
      this.proxyClient = proxyClient;
      this.tripsStorage = tripsStorage;
   }


   public ResponseEntity<Object> cancelTripClient(UUID clientId, UUID tripId) {
      // check pending first
      TripForDB trip = tripsStorage.getRemovePending(tripId);
      if (trip != null) {
         // calculate penalty for the client, but no driver involved
         return new ResponseEntity<>(HttpStatus.OK);
      }

      // proceed with ongoing trips
      trip = tripsStorage.getRemoveOngoing(tripId);
      if (trip == null) {
         return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
      }

      if (trip.getStatus() == TripForDB.TripStatus.ACCEPTED) {
         // calculate penalty for the client
         trip.setStatus(TripForDB.TripStatus.CANCELLED);

         // notify the driver the trip is cancelled
         proxyClient.tripCancelled(
               ServiceNames.Drivers.getLabel(),
               trip.getDriverId(),
               trip.getTripId());

         // trip is already deleted here
         return new ResponseEntity<>(HttpStatus.OK);
      }

      // For the moment client can't cancel ongoing trip
      return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
   }

   public ResponseEntity<Object> cancelTripDriver(UUID driverId, UUID tripId) {
      // proceed with ongoing trips
      TripForDB trip = tripsStorage.getRemoveOngoing(tripId);
      if (trip == null || trip.getDriverId() != driverId) {
         // already cancelled/completed trip or some error

         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      if (trip.getStatus() == TripForDB.TripStatus.ACCEPTED) {
         // calculate penalty for the driver
         trip.setStatus(TripForDB.TripStatus.CANCELLED);

         // notify the driver the trip is cancelled
         proxyClient.tripCancelled(
               ServiceNames.Clients.getLabel(),
               trip.getClientId(),
               trip.getTripId());

         // trip is already deleted here
         return new ResponseEntity<>(HttpStatus.OK);
      } else {

         // For IN_PROGRESS trips complete trip only is possible
         return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
      }
   }

   public ResponseEntity<TripForDriver> startTripDriver(UUID driverId, UUID tripId) {
      // proceed with ongoing trips
      TripForDB trip = tripsStorage.getRemoveOngoing(tripId);
      if (trip == null ||  trip.getDriverId() != driverId) {
         return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
      }

      trip.setStatus(TripForDB.TripStatus.IN_PROGRESS);
      tripsStorage.addOngoingTrip(trip);

      TripForDriver tripForDriver = new TripForDriver(trip.getClientId(), tripId, trip.getToLocation());
      return new ResponseEntity<>(tripForDriver, HttpStatus.OK);
   }

   public ResponseEntity<Object> completeTripDriver(UUID driverId, UUID tripId) {
      TripForDB trip = tripsStorage.getRemoveOngoing(tripId);
      if (trip == null || trip.getDriverId() != driverId
            || trip.getStatus() != TripForDB.TripStatus.IN_PROGRESS) {
         return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
      }

      trip.setFinished();

      proxyClient.tripCompleted(ServiceNames.Clients.getLabel(), trip.getClientId(), trip.getTripId());
      return new ResponseEntity<>(HttpStatus.OK);
   }
}
