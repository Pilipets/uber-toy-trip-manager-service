package uber.location_service.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uber.location_service.structures.SupplyInstance;
import uber.location_service.structures.Temp;
import uber.location_service.utils.JsonParseHandler;
import uber.location_service.structures.GeoPoint;
import uber.location_service.utils.JsonParseHandler;

import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping(path="/supply_location")
public class SupplyLocationController {
   @GetMapping(path="/get_closest")
   public ResponseEntity<Object> getClosestHandler(
         @RequestParam(value = "geoPoint") String geoPointString) {
      final GeoPoint geoPoint = JsonParseHandler.parseJson(geoPointString, GeoPoint.class);
      if (geoPoint == null) {
         return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
      }
      return new ResponseEntity<>(geoPoint, HttpStatus.OK);
   }

   @GetMapping(path="/get_closest_in_radius")
   public ResponseEntity<Object> getClosestInRadiusHandler(
         @RequestParam(value = "geoPoint") String geoPointString) {
      final GeoPoint geoPoint = JsonParseHandler.parseJson(geoPointString, GeoPoint.class);
      if (geoPoint == null) {
         return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
      }
      List<GeoPoint> arr = new ArrayList<>();
      arr.add(geoPoint); arr.add(geoPoint); arr.add(geoPoint);
      return new ResponseEntity<>(arr, HttpStatus.OK);
   }

   @GetMapping(path="/get_temp")
   public ResponseEntity<Object> greeting2(
         @RequestParam(value = "temp") String tempString) {
      final Temp temp = JsonParseHandler.parseJson(tempString, Temp.class);
      if (temp == null) {
         return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
      }
      return new ResponseEntity<>(temp, HttpStatus.OK);
   }

   @PostMapping(path="/update_supply")
   public ResponseEntity<Object> updateSupplyLocation(
         @RequestParam(value = "instance") String supplyString) {
      final SupplyInstance ins = JsonParseHandler.parseJson(supplyString, SupplyInstance.class);
      if (ins == null) {
         return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
      }
      return new ResponseEntity<>(ins, HttpStatus.OK);
   }
}
