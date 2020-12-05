package uber.trip_manager_service.services;

import org.springframework.stereotype.Service;
import uber.trip_manager_service.structures.internal.LocationPoint;
import uber.trip_manager_service.structures.internal.TripForDB;
import uber.trip_manager_service.structures.external.GeoPoint;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TripsStorageDriver {
   private final int REQUESTS_COUNT = 100;
   private ConcurrentHashMap<UUID, TripForDB> cacheMap;

   // Normally here will be DB
   private ConcurrentHashMap<UUID, TripForDB> ongoingMap;

   TripsStorageDriver() {
      cacheMap = new ConcurrentHashMap<>(REQUESTS_COUNT);
      ongoingMap = new ConcurrentHashMap<>(REQUESTS_COUNT);
   }

   public TripForDB addPendingTrip(UUID clientId, LocationPoint fromPoint, LocationPoint toPoint) {
      TripForDB trip = new TripForDB(clientId, fromPoint, toPoint);
      cacheMap.put(trip.getTripId(), trip);
      return trip;
   }

   public void addOngoingTrip(TripForDB trip) {
      ongoingMap.put(trip.getTripId(), trip);
   }

   public TripForDB getRemovePending(UUID tripId) {
      return cacheMap.remove(tripId);
   }

   public TripForDB getPending(UUID tripId) {
      return cacheMap.get(tripId);
   }

   public TripForDB getOngoing(UUID tripId) {
      return ongoingMap.get(tripId);
   }

   public TripForDB getRemoveOngoing(UUID tripId) {
      return ongoingMap.remove(tripId);
   }

   public TripForDB getTrip(UUID tripId) {
      return cacheMap.getOrDefault(tripId, null);
   }
}
