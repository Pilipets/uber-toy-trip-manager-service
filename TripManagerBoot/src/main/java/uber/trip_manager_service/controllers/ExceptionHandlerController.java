package uber.trip_manager_service.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

   @ExceptionHandler(JsonProcessingException.class)
   public ResponseEntity<Object> handleJsonException(
         JsonProcessingException ex, WebRequest webRequest) {

      Map<String, Object> body = new HashMap<>();
      body.put("timestamp", LocalDateTime.now());
      body.put("message", ex.getOriginalMessage());

      return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
   }

   @ExceptionHandler(HttpClientErrorException.class)
   public ResponseEntity<Object> handleHttpException(
         HttpClientErrorException ex, WebRequest webRequest) {

      Map<String, Object> body = new HashMap<>();
      body.put("timestamp", LocalDateTime.now());
      body.put("message", ex.getResponseBodyAsString());

      return new ResponseEntity<>(body, HttpStatus.REQUEST_TIMEOUT);
   }
}