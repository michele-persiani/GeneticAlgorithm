package examples.sensors.model.cost;

import examples.sensors.model.Environment;

public interface CostModel
{
    double getEnvironmentCost(Environment env);
}
