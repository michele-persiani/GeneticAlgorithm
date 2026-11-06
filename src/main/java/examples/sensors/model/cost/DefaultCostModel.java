package examples.sensors.model.cost;

import examples.sensors.model.Environment;
import examples.sensors.model.Location;
import examples.sensors.model.Sensor;

public class DefaultCostModel implements CostModel
{
    private double notCoveredCost;
    private int minNumSensorsForCovered;

    private double measurementSlope;
    private final double cityCost;
    private final double lakeCost;

    public DefaultCostModel(
            double notCoveredCost,      // cost of not covering a location with at least minNumSensorsForCovered sensors
            int minNumSensorsForCovered, // minimum number of sensors required to cover a location
            double measurementSlope, // slope coefficients for measurements costs
            double cityCost,
            double lakeCost
    )
    {
        this.notCoveredCost = notCoveredCost;
        this.minNumSensorsForCovered = minNumSensorsForCovered;
        this.measurementSlope = measurementSlope;
        this.cityCost = cityCost;
        this.lakeCost = lakeCost;
    }


    /**
     * Cost of measuring a location with the given sensor
     * @param env
     * @param sensor
     * @param location
     * @return
     */
    public double measurementCost(Environment env, Sensor sensor, Location location)
    {
        double x = env.getDistance(sensor, location);
        return Math.exp(measurementSlope * x) - 1 + sensor.precision;

    }

    /**
     * Cost of deploying a sensor at the given location. It could be different depending on the type of terrain
     * @param env
     * @param sensor
     * @return
     */
    public double deploymentCost(Environment env, Sensor sensor)
    {
        double cost = sensor.cost;

        cost += env.isInLake(sensor) ? lakeCost : 0;
        cost += env.isInCity(sensor) ? cityCost : 0;

        return cost;
    }

    /**
     * Cost of a point of interest. It could be different depending on how many sensors are assigned to it
     *
     * @param env
     * @param pointOfInterest
     * @return
     */
    public double pointOfInterestCost(Environment env, Location pointOfInterest)
    {
        return env.getAssignedSensors(pointOfInterest).size() >= minNumSensorsForCovered ? 0.0 : notCoveredCost;
    }


    public double sensorTotalCost(Environment env, Sensor sensor)
    {
        // deployment cost
        double deploymentCost = deploymentCost(env, sensor);

        // measurement cost for all locations
        double measurementCost = env.getAssignments(sensor)
                .stream()
                .reduce(0d, (acc, location) -> acc + measurementCost(env, sensor, location), Double::sum);

        return deploymentCost + measurementCost;
    }

    public double getEnvironmentCost(Environment env)
    {
        double sensorsCost = env.getSensors()
                .stream()
                .mapToDouble(s -> sensorTotalCost(env, s))
                .sum();

        double poisCost = env.getPOIs()
                .stream()
                .mapToDouble(poi -> pointOfInterestCost(env, poi))
                .sum();

        return sensorsCost + poisCost;
    }
}
