package uber.location_service.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.javatuples.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uber.location_service.services.SupplyLocationImpl;
import uber.location_service.structures.GeoPoint;
import uber.location_service.structures.SupplyInstance;
import uber.location_service.structures.Temp;

import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping(path="/supply_location_service")
public class SupplyLocationController {
   SupplyLocationImpl impl;

   public SupplyLocationController(final SupplyLocationImpl impl) {
      this.impl = impl;
   }

   @GetMapping(path="/get_closest")
   public ResponseEntity<Object> getClosestHandler(
         @RequestParam(value = "geoPoint") String geoPointString) throws JsonProcessingException {
      final GeoPoint geoPoint = new ObjectMapper().readValue(geoPointString, GeoPoint.class);

      List<Pair<SupplyInstance, Double>> arr = impl.getClosestSupply(geoPoint);
      return new ResponseEntity<>(arr, HttpStatus.OK);
   }

   @GetMapping(path="/get_closest_in_radius")
   public ResponseEntity<Object> getClosestInRadiusHandler(
         @RequestParam(value = "geoPoint") String geoPointString) throws JsonProcessingException {
      final GeoPoint geoPoint = new ObjectMapper().readValue(geoPointString, GeoPoint.class);

      List<Pair<SupplyInstance, Double>> arr = impl.getRadiusSupply(geoPoint);
      return new ResponseEntity<>(arr, HttpStatus.OK);
   }

   @PostMapping(path="/update_supply")
   public ResponseEntity<Object> updateSupplyInstance(
         @RequestParam(value = "instance") String supplyString) throws JsonProcessingException {
      final SupplyInstance ins = new ObjectMapper().readValue(supplyString, SupplyInstance.class);

      impl.update(ins);
      return new ResponseEntity<>(HttpStatus.OK);
   }

   @GetMapping(path="/get_supply")
   public SupplyInstance getSupplyInstance(@RequestParam UUID uuid) {
      return impl.get(uuid);
   }
}
