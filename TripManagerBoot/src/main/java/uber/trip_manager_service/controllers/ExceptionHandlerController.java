package uber.trip_manager_service.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.net.ConnectException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

   private Map<String, Object> getBasicExceptionBody() {
      Map<String, Object> body = new HashMap<>();
      body.put("timestamp", LocalDateTime.now());
      return body;
   }

   @ExceptionHandler(JsonProcessingException.class)
   public ResponseEntity<Object> handleJsonException(
         JsonProcessingException ex, WebRequest webRequest) {

      Map<String, Object> body = getBasicExceptionBody();
      body.put("message", ex.getOriginalMessage());

      return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
   }

   @ExceptionHandler({HttpClientErrorException.class})
   public ResponseEntity<Object> handleHttpException(
         HttpClientErrorException ex, WebRequest webRequest) {

      Map<String, Object> body = getBasicExceptionBody();
      body.put("message", ex.getResponseBodyAsString());
      return new ResponseEntity<>(body, HttpStatus.REQUEST_TIMEOUT);
   }

   @ExceptionHandler({FeignException.class})
   public ResponseEntity<Object> handleFeignException(
         FeignException ex, WebRequest webRequest) {

      Map<String, Object> body = getBasicExceptionBody();
      body.put("message", ex.request().url());
      body.put("details", "Unable to communicate between other microservices");

      return new ResponseEntity<>(body, HttpStatus.SERVICE_UNAVAILABLE);
   }

   @ExceptionHandler({Exception.class})
   public ResponseEntity<Object> handleAnyException(
         Exception ex, WebRequest webRequest) {

      Map<String, Object> body = getBasicExceptionBody();
      body.put("message", ex.getMessage());
      body.put("details", "Something bad happened - unable to identify");

      return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
   }
}