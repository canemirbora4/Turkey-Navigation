public class City {

    private String cityName;
    private int x;
    private int y;
    private double distanceValue;
    private City Parent;

    /**
     *
     * @param cityName keeps the cities name
     * @param x keeps the X coordinate of that city
     * @param y keeps the Y coordinate of that city
     */

    City(String cityName, int x, int y) {
        this.cityName = cityName;
        this.x = x;
        this.y = y;
    }

    public String getCityName() {
        return cityName;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     *
     * @param distanceValue keeps the length of the shortest path from the that city to the starting city
     * @param cityName keeps the cities name
     * @param Parent keeps the city that comes after that city on the shortest path
     */

    City(double distanceValue, String cityName, City Parent){
        this.cityName=cityName;
        this.distanceValue=distanceValue;
        this.Parent=Parent;
    }


    public double getDistanceValue() {
        return distanceValue;
    }

    public City getParent() {
        return Parent;
    }

    public void setDistanceValue(double distanceValue) {
        this.distanceValue = distanceValue;
    }

    public void setParent(City parent) {
        Parent = parent;
    }
}
