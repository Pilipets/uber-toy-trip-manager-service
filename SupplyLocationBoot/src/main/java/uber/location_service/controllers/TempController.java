package uber.location_service.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uber.location_service.structures.Temp;

@RestController()
@RequestMapping(path="/supply_location_service")
public class TempController {
   @GetMapping(path="/get_temp")
   public ResponseEntity<Object> greeting2(
         @RequestParam(value = "temp") String tempString) throws JsonProcessingException {
      final Temp temp = new ObjectMapper().readValue(tempString, Temp.class);

      return new ResponseEntity<>(temp, HttpStatus.OK);
   }
}