package uber.trip_manager_service.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HttpUtils {
   public static <T> boolean isValidResponse(ResponseEntity<T> resp) {
      return resp != null &&
            resp.getStatusCode().value() >= HttpStatus.OK.value() &&
            resp.getStatusCode().value() <= HttpStatus.IM_USED.value();
   }
}
