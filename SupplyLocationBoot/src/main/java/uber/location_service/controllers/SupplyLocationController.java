package uber.location_service.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uber.location_service.services.SupplyLocationImpl;
import uber.location_service.structures.GeoPoint;
import uber.location_service.structures.SupplyInstance;

import java.util.List;
import java.util.UUID;

/**
 * This is the API that exists at the moment due to the chosen implementation model,
 * but eventually should be replaced with another approach. SupplyLocationService must not
 * consume any data from the Kafka - instead use the KD-tree or local-sensitive hashing
 * with Google S2 library to get the polygons, that requested geolocation intersects,
 * then make "MapReduce(...)" call to the Filter service in turn to the HDFS to get the
 * filtered drivers in the intersected polygons.
 */
@RestController()
@RequestMapping(path="/supply-location-service")
public class SupplyLocationController {
   private final SupplyLocationImpl impl;
   private final ObjectMapper jsonMapper = new ObjectMapper();
   private final Logger logger = LoggerFactory.getLogger(SupplyLocationController.class);

   @Autowired
   public SupplyLocationController(final SupplyLocationImpl impl) {
      this.impl = impl;
   }

   @GetMapping(path="/get-closest")
   public ResponseEntity<Object> getClosestHandler(GeoPoint geoPoint) {

      List<SupplyInstance> arr = impl.getClosestSupply(geoPoint);
      return new ResponseEntity<>(arr, HttpStatus.OK);
   }

   @GetMapping(path="/get-closest-in-radius")
   public ResponseEntity<Object> getClosestInRadiusHandler(GeoPoint geoPoint) {
      List<SupplyInstance> arr = impl.getRadiusSupply(geoPoint);
      return new ResponseEntity<>(arr, HttpStatus.OK);
   }

   @PostMapping(path="/update-supply")
   public ResponseEntity<Object> updateSupplyInstance(@RequestBody SupplyInstance ins) {

      impl.updateSupply(ins);
      return new ResponseEntity<>(HttpStatus.OK);
   }

   @GetMapping(path="/get-location")
   public ResponseEntity<Object> getSupplyLocation(@RequestParam(value = "id") UUID id) {
      GeoPoint location = impl.getSupplyLocation(id);
      if (location == null) {
         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(location, HttpStatus.OK);
   }
}
