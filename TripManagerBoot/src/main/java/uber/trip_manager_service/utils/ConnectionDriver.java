package uber.trip_manager_service.utils;

import uber.trip_manager_service.structures.internal.TripEntity;
import uber.trip_manager_service.structures.external.GeoPoint;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionDriver {
   private ConcurrentHashMap<UUID, TripEntity> map;

   public UUID addPendingTrip(UUID clientId, GeoPoint where, GeoPoint to) {
      TripEntity trip = new TripEntity(clientId, where, to);
      map.put(trip.getTripId(), trip);
      return trip.getTripId();
   }
}
