package uber.trip_manager_service.structures.internal;

import java.io.Serializable;
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
   int carType = 0;

   public int getCapacity() {
      return capacity;
   }

   public int getCarType() {
      return carType;
   }

   public RequestTripParams() {

   }

   public RequestTripParams(RequestTripParams other) {
      this.capacity = other.capacity;
      this.carType = other.carType;
   }
}