package uber.trip_manager_service.structures.internal;

public class FilterTripParams extends RequestTripParams {
   boolean accepts_rides = true;
   boolean on_the_ride = false;
   public FilterTripParams(RequestTripParams params) {
      super(params);
   }
}
