package uber.trip_manager_service.structures.external;

public class GeoPoint {
   private double latitude, longitude; // latitude, longitude in radians
   public double getLatitude() {
      return latitude;
   }

   public double getLongitude() {
      return longitude;
   }

   public GeoPoint(double latitude, double longitude) {
      this.latitude = latitude;
      this.longitude = longitude;
   }
}