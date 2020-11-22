package uber.trip_manager_service.structures.internal;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

public class FilterTripParams extends RequestTripParams {
   boolean acceptsRides = true;
   boolean onTheRide = false;
   public FilterTripParams(RequestTripParams params) {
      super(params);
   }
}

class RequestTripParams {
   int capacity = 0;
   List<Integer> carTypes = null;

   public int getCapacity() {
      return capacity;
   }

   public List<Integer> getCarTypes() {
      return carTypes;
   }

   public RequestTripParams() {

   }

   public RequestTripParams(RequestTripParams other) {
      this.capacity = other.capacity;
      this.carTypes = other.carTypes;
   }
}