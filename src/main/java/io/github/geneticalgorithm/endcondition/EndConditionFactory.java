package io.github.geneticalgorithm.endcondition;

import java.util.Arrays;

public class EndConditionFactory
{
    public <T> EndCondition<T> createEpochNumReached(int maxEpoch)
    {
        return epoch -> epoch.getEpochNum() >= maxEpoch;
    }

    public <T> EndCondition<T> createAverageFitnessReached(double averageFitness)
    {
        return epoch -> epoch.getPopulation()
                .values()
                .stream()
                .mapToDouble(v -> v)
                .average()
                .orElse(Double.NEGATIVE_INFINITY) >= averageFitness;
    }

    public <T> EndCondition<T> createMaximumFitnessReached(double maximumFitness)
    {
        return epoch -> epoch.getPopulation()
                .values()
                .stream()
                .max(Double::compare)
                .orElse(Double.NEGATIVE_INFINITY) >= maximumFitness;
    }

    public <T> EndCondition<T> createMinimumFitnessReached(double minimumFitness)
    {
        return epoch -> epoch.getPopulation()
                .values()
                .stream()
                .min(Double::compare)
                .orElse(Double.NEGATIVE_INFINITY) >= minimumFitness;
    }

    public <T> EndCondition<T> and(EndCondition<T>... conditions)
    {
        return epoch -> Arrays.stream(conditions).allMatch(c -> c.isTrainingFinished(epoch));
    }

    public <T> EndCondition<T> or(EndCondition<T>... conditions)
    {
        return epoch -> Arrays.stream(conditions).anyMatch(c -> c.isTrainingFinished(epoch));
    }
}
