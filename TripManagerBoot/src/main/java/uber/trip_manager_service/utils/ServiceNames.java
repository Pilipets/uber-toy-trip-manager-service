package uber.trip_manager_service.utils;

public enum ServiceNames {
   Drivers("drivers"),
   Clients("clients");
   final String label;

   public String getLabel() {
      return label;
   }

   private ServiceNames(String label) {
      this.label = label;
   }
}
