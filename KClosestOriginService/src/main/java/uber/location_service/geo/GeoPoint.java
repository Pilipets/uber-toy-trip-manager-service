package uber.location_service.geo;

public class GeoPoint {
   private double radLat, radLon; // latitude, longitude in radians
   private double degLat, degLon;  // latitude, longitude in degrees

   private GeoPoint () {
   }

   public static GeoPoint fromDegrees(double latitude, double longitude) {
      GeoPoint result = new GeoPoint();
      result.radLat = Math.toRadians(latitude);
      result.radLon = Math.toRadians(longitude);
      result.degLat = latitude;
      result.degLon = longitude;
      return result;
   }

   /**
    * @param latitude the latitude, in radians.
    * @param longitude the longitude, in radians.
    */
   public static GeoPoint fromRadians(double latitude, double longitude) {
      GeoPoint result = new GeoPoint();
      result.radLat = latitude;
      result.radLon = longitude;
      result.degLat = Math.toDegrees(latitude);
      result.degLon = Math.toDegrees(longitude);
      return result;
   }

   public double getLatDegrees() {
      return degLat;
   }

   public double getLonDegrees() {
      return degLon;
   }

   public double getLatRadians() {
      return radLat;
   }

   public double getLonRadians() {
      return radLon;
   }

   @Override
   public String toString() {
      return "(" + degLat + "\u00B0, " + degLon + "\u00B0) = (" +
            radLat + " rad, " + radLon + " rad)";
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