package uber.trip_manager_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uber.trip_manager_service.structures.internal.FilterTripParams;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "db-service",
      url = "http://localhost:8083/db_service")
public interface DbClient {
   @PostMapping(path="/filter_supply")
   ResponseEntity<List<UUID>> filterSupply(@SpringQueryMap FilterTripParams params,
                                           @RequestBody List<UUID> supply_list);
}
