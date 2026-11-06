package examples.sensors.model;



public class Sensor extends Location
{
    public double cost;
    public double precision;

    public Sensor(double positionX, double positionY, double cost, double precision)
    {
        super(positionX, positionY);
        this.cost = cost;
        this.precision = precision;
    }

    public Sensor clone()
    {
        return new Sensor(getPositionX(), getPositionY(), cost, precision);
    }
}
