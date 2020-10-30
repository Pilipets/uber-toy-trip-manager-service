package uber.location_service.structures;

public class GeoPoint {
   private double radLat, radLon; // latitude, longitude in radians

   private GeoPoint() {

   }

   public GeoPoint(double latitude, double longitude) {
      this.radLat = latitude;
      this.radLon = longitude;
   }

   /**
    * @param latitude the latitude, in radians.
    * @param longitude the longitude, in radians.
    */
   public static GeoPoint fromRadians(double latitude, double longitude) {
      GeoPoint result = new GeoPoint();
      result.radLat = latitude;
      result.radLon = longitude;
      return result;
   }

   public double getLatRadians() {
      return radLat;
   }

   public double getLonRadians() {
      return radLon;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      GeoPoint geoPoint = (GeoPoint) o;
      if (Double.compare(geoPoint.radLat, radLat) != 0) return false;
      if (Double.compare(geoPoint.radLon, radLon) != 0) return false;
      return true;
   }

   @Override
   public String toString() {
      return "[" + radLat + ", " + radLon + "]";
   }

   /**
    * Computes the great circle distance between this GeoLocation instance
    * and the location argument.
    * @param radius the radius of the sphere, for the Earth ~6371.01 kilometers.
    * @return the distance, measured in the same unit as the radius
    * argument.
    */
   public double distanceTo(GeoPoint location, double radius) {
      return Math.acos(Math.sin(radLat) * Math.sin(location.radLat) +
            Math.cos(radLat) * Math.cos(location.radLat) *
                  Math.cos(radLon - location.radLon)) * radius;
   }
}