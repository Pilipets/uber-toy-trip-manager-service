package uber.trip_manager_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uber.trip_manager_service.structures.SupplyReturnType;

import java.util.List;

@FeignClient(name = "supply-location-service",
      url = "http://localhost:8080/supply_location_service")
public interface SupplyLocationClient {

   @GetMapping(path="/get_closest")
   List<SupplyReturnType> getClosestSupply(
         @RequestParam(value = "geoPoint") String geoPoint);

}
