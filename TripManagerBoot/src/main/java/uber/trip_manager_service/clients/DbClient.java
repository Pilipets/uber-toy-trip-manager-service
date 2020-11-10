package uber.trip_manager_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;
import uber.trip_manager_service.structures.FilterTripParams;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "db-service",
      url = "http://localhost:8083/db_service")
public interface DbClient {
   @PostMapping(path="/filter_supply")
   List<UUID> filterSupply(@SpringQueryMap FilterTripParams params,
                           List<UUID> supply_list);
}
