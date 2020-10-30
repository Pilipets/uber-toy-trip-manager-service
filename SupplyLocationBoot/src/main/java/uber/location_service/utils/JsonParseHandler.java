package uber.location_service.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import uber.location_service.structures.GeoPoint;

public class JsonParseHandler {
   public static <T> T parseJson(String content, Class<T> valueType)  {
      try {
         return new ObjectMapper().readValue(content, valueType);
      } catch (JsonProcessingException ex) {
         System.out.println(ex.getMessage());
         ex.printStackTrace();
         return null;
      }
   }
}
