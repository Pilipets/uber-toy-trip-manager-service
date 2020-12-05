package uber.trip_manager_service.utils;

public enum ServiceNames {
   Drivers("driver"),
   Clients("rider");
   final String label;

   public String getLabel() {
      return label;
   }

   private ServiceNames(String label) {
      this.label = label;
   }
}
