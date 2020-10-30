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

@RestController()
@RequestMapping(path="/supply_location")
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

   /*
   @GetMapping(path="/get_temp")
   public ResponseEntity<Object> greeting2(
         @RequestParam(value = "temp") String tempString) throws JsonProcessingException {
      final Temp temp = new ObjectMapper().readValue(tempString, Temp.class);

      return new ResponseEntity<>(temp, HttpStatus.OK);
   }*/

   @PostMapping(path="/update_supply")
   public ResponseEntity<Object> updateSupplyLocation(
         @RequestParam(value = "instance") String supplyString) throws JsonProcessingException {
      final SupplyInstance ins = new ObjectMapper().readValue(supplyString, SupplyInstance.class);

      impl.update(ins);
      return new ResponseEntity<>(HttpStatus.OK);
   }
}
