package uber.trip_manager_service.structures.external;

public class SupplyInstance {
   protected String id;
   protected GeoPoint location;

   public SupplyInstance() {
   }

   public String getId() {
      return id;
   }

   public GeoPoint getLocation() {
      return location;
   }
}
