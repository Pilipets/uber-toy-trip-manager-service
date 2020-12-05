package uber.trip_manager_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uber.trip_manager_service.structures.external.GeoPoint;
import uber.trip_manager_service.structures.external.SupplyInstance;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "supply-location-service",
      url = "https://toy-supply-location-service.herokuapp.com")
public interface SupplyLocationClient {

   @GetMapping(path="/get-closest")
   ResponseEntity<List<SupplyInstance>> getClosestSupply(
         @SpringQueryMap GeoPoint geoPoint);

   @GetMapping(path="/get-closest-in-radius")
   ResponseEntity<List<SupplyInstance>> getClosestInRadius(
         @SpringQueryMap GeoPoint geoPoint);

   @GetMapping(path ="/get-location")
   ResponseEntity<GeoPoint> getSupplyLocation(
         @RequestParam(value = "id") UUID id);
}
