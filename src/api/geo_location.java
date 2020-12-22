package api;
/**
 * This interface represents a geo location x,y,z, aka Point3D
 */
public interface geo_location {
    /**
     * This method represents a location x, aka Point3D
     * @return x data
     */
    public double x();
    /**
     * This method represents a location y, aka Point3D
     * @return y data
     */
    public double y();
    /**
     * This method represents a location z, aka Point3D
     * @return z data
     */
    public double z();
    /**
     * This method represents a distance calculation
     * @param g - 3D point
     * @return distance
     */
    public double distance(geo_location g);
}