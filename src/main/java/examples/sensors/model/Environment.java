package examples.sensors.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Environment
{

    public enum PositionType
    {
        LAND, LAKE, CITY
    }

    private final List<Location> pois = new ArrayList<>();

    private final List<Sensor> sensors = new ArrayList<>();

    private final List<Tuple<Sensor, Location>> assignments = new ArrayList<>();


    private final double minX;
    private final double minY;
    private final double maxX;
    private final double maxY;

    private final List<Circle> lakes = new ArrayList<>();

    private final List<Circle> cities = new ArrayList<>();



    public Environment(double minX, double maxX, double minY, double maxY)
    {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public PositionType getPositionType(double x, double y)
    {
        if(x < minX || x > maxX || y < minY || y > maxY)
            throw new IllegalArgumentException("Invalid coordinates");
        boolean isLake = lakes.stream().anyMatch(lake -> lake.contains(x, y));
        boolean isCity = cities.stream().anyMatch(city -> city.contains(x, y));

        if(isCity)
            return PositionType.CITY;
        if(isLake)
            return PositionType.LAKE;
        return PositionType.LAND;
    }

    public List<Double> getDistanceToLocations(double fromX, double fromY)
    {
        if(fromX < minX || fromX > maxX || fromY < minY || fromY > maxY)
            throw new IllegalArgumentException("Invalid coordinates");
        return pois.stream()
                .map(loc -> Math.sqrt((loc.getPositionX() - fromX) * (loc.getPositionX() - fromX) + (loc.getPositionY() - fromY) * (loc.getPositionY() - fromY)))
                .toList();
    }

    public double getMinX()
    {
        return minX;
    }

    public double getMaxX()
    {
        return maxX;
    }

    public double getMinY()
    {
        return minY;
    }

    public double getMaxY()
    {
        return maxY;
    }

    public void addLake(Circle circle)
    {
        lakes.add(circle);
    }

    public List<Circle> getLakes()
    {
        return Collections.unmodifiableList(lakes);
    }

    public List<Circle> getCities()
    {
        return Collections.unmodifiableList(cities);
    }

    public void addCity(Circle circle)
    {
        cities.add(circle);
    }

    public void addSensor(Sensor sensor)
    {
        sensors.add(sensor);
    }

    public List<Sensor> getSensors()
    {
        return Collections.unmodifiableList(sensors);
    }

    public void addPOI(Location poi)
    {
        pois.add(poi);
    }

    public List<Location> getPOIs()
    {
        return Collections.unmodifiableList(pois);
    }

    public void addAssignment(Sensor sensor, Location poi)
    {
        if(!sensors.contains(sensor))
            throw new IllegalArgumentException("Sensor not found");
        if(!pois.contains(poi))
            throw new IllegalArgumentException("POI not found");
        Tuple<Sensor, Location> tp = new Tuple<>(sensor, poi);
        if(!assignments.contains(tp))
            assignments.add(tp);
    }

    public void removeAssignment(Sensor sensor, Location poi)
    {
        if(!sensors.contains(sensor))
            throw new IllegalArgumentException("Sensor not found");
        if(!pois.contains(poi))
            throw new IllegalArgumentException("POI not found");
        Tuple<Sensor, Location> tp = new Tuple<>(sensor, poi);
        assignments.remove(tp);
    }

    public List<Location> getAssignments(Sensor sensor)
    {
        return assignments.stream()
                .filter(v -> v.first == sensor)
                .map(v -> v.second)
                .toList();
    }

    public List<Sensor> getAssignedSensors(Location poi)
    {
        return assignments.stream()
                .filter(tp -> tp.second == poi)
                .map(tp -> tp.first)
                .toList();
    }

    public boolean isAssignedTo(Sensor sensor, Location poi)
    {
        return assignments.stream()
                .anyMatch(v -> v.first == sensor && v.second == poi);
    }

    public void clearAssignments()
    {
        assignments.clear();
    }

    public void clearSensors()
    {
        sensors.clear();
    }

    public double getDistance(Location l0, Location l1)
    {
        return Math.sqrt(Math.pow(l0.getPositionX() - l1.getPositionX(), 2) +  Math.pow(l0.getPositionY() - l1.getPositionY(), 2));
    }

    public boolean isInCity(Location l)
    {
        return cities.stream().anyMatch(city -> city.contains(l.getPositionX(), l.getPositionY()));
    }

    public boolean isInLake(Location l)
    {
        return !isInCity(l) && lakes.stream().anyMatch(lake -> lake.contains(l.getPositionX(), l.getPositionY()));
    }
}
