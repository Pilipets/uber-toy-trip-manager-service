package uber.trip_manager_service.structures;

import java.io.Serializable;

public class GeoPoint implements Serializable {
   private double latitude, longitude; // latitude, longitude in radians

   public GeoPoint() {

   }

   public double getLatitude() {
      return latitude;
   }

   public double getLongitude() {
      return longitude;
   }

   @Override
   public String toString() {
      return "[" + latitude + ", " + longitude + "]";
   }
}