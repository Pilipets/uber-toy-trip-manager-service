package uber.trip_manager_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uber.trip_manager_service.structures.external.GeoPoint;
import uber.trip_manager_service.structures.external.SupplyInstance;

import java.util.List;

@FeignClient(name = "supply-location-service",
      url = "http://localhost:8080/supply-location-service")
public interface SupplyLocationClient {

   @GetMapping(path="/get-closest")
   ResponseEntity<List<SupplyInstance>> getClosestSupply(
         @SpringQueryMap GeoPoint geoPoint);

   @GetMapping(path="/get-closest-in-radius")
   ResponseEntity<List<SupplyInstance>> getClosestInRadius(
         @SpringQueryMap GeoPoint geoPoint);

}
