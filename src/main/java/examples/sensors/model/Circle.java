package examples.sensors.model;

public class Circle
{
    public final double x;
    public final double y;
    public final double radius;

    public Circle(double x, double y, double radius)
    {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public boolean contains(double x, double y)
    {
        return Math.sqrt((x - this.x) * (x - this.x) + (y - this.y) * (y - this.y)) <= radius;
    }

    public double area()
    {
        return Math.PI * radius * radius;
    }

    public double perimeter()
    {
        return 2 * Math.PI * radius;
    }
}
