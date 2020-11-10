package uber.trip_manager_service.structures;

class RequestTripParams {
   int capacity = 0;
   int typeId = 0;
   public int getCapacity() {
      return capacity;
   }

   public int getTypeId() {
      return typeId;
   }

   RequestTripParams() {

   }
}
public class FilterTripParams extends RequestTripParams {
   boolean accepts_rides = true;
   boolean on_the_ride = false;
}
