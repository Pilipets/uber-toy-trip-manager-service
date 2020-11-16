package uber.trip_manager_service.structures.external;

import java.io.Serializable;

public class GeoPoint implements Serializable {
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