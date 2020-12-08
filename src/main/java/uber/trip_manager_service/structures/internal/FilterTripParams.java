package uber.trip_manager_service.structures.internal;

public class FilterTripParams extends RequestTripParams {
   boolean accepts_rides = true;

   public boolean isAcceptsRides() {
      return accepts_rides;
   }

   public boolean isOnTheRide() {
      return on_the_ride;
   }

   boolean on_the_ride = false;


   public FilterTripParams(RequestTripParams params) {
      super(params);
   }
}

class RequestTripParams {
   int capacity = 0;
   int car_type = 0;

   public int getCapacity() {
      return capacity;
   }

   public int getCarType() {
      return car_type;
   }

   public RequestTripParams() {

   }

   public RequestTripParams(RequestTripParams other) {
      this.capacity = other.capacity;
      this.car_type = other.car_type;
   }
}