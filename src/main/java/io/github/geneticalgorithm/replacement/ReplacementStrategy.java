package io.github.geneticalgorithm.replacement;

import io.github.geneticalgorithm.Epoch;
import io.github.geneticalgorithm.callback.Callback;
import io.github.geneticalgorithm.chromosome.Chromosome;

import java.util.Map;

public interface ReplacementStrategy<T> extends Callback<T>
{
    /**
     * Replaces the current population with the offsprings obtained through selection-crossover-mutation
     * @param population current population to replace
     * @param offspring offspring to replace the population with
     */
    void replace(Map<Chromosome<T>, Double> population, Map<Chromosome<T>, Double> offspring);


    @Override
    default void onTrainingStart()
    {

    }

    @Override
    default void onEpochStart(int epochNum)
    {

    }

    @Override
    default void onEpochFinish(Epoch<T> epoch)
    {

    }

    @Override
    default void onTrainingFinish(Epoch<T> lastEpoch)
    {

    }
}
