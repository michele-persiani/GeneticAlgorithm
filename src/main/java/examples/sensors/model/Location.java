package examples.sensors.model;

public class Location
{
    private double positionX;
    private double positionY;

    public Location(double positionX, double positionY)
    {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public double getDistance(Location other)
    {
        return Math.sqrt(Math.pow(positionX - other.positionX, 2) + Math.pow(positionY - other.positionY, 2));
    }

    public double getPositionX()
    {
        return positionX;
    }

    public double getPositionY()
    {
        return positionY;
    }
}
