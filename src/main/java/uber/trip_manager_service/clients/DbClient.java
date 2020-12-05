package uber.trip_manager_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uber.trip_manager_service.structures.internal.FilterTripParams;
import uber.trip_manager_service.structures.internal.TripForDB;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "db-service",
      url = "https://uber-main-db.herokuapp.com")
public interface DbClient {
   @PostMapping(path="/api/services/helper/reduce")
   ResponseEntity<List<UUID>> filterSupply(
         @SpringQueryMap FilterTripParams params,
         @RequestBody List<UUID> supply_list);

   @PostMapping(path="/api/services/trip")
   ResponseEntity<Object> saveTrip(@RequestBody TripForDB tripForDB);

   @PutMapping(path ="/api/services/driver/{id}")
   ResponseEntity<Object> updateDriverStatus(
         @PathVariable("id") UUID driverId,
         @RequestBody boolean onTheRide);
}
