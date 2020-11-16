package uber.trip_manager_service.utils;

import uber.trip_manager_service.structures.internal.TripForDB;
import uber.trip_manager_service.structures.external.GeoPoint;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionDriver {
   private ConcurrentHashMap<UUID, TripForDB> map;

   public UUID addPendingTrip(UUID clientId, GeoPoint where, GeoPoint to) {
      TripForDB trip = new TripForDB(clientId, where, to);
      map.put(trip.getTripId(), trip);
      return trip.getTripId();
   }

   public TripForDB getRemoveTrip(UUID tripId) {
      return map.remove(tripId);
   }

   public TripForDB getTrip(UUID tripId) {
      return map.getOrDefault(tripId, null);
   }
}
