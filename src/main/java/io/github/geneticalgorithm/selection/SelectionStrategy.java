package io.github.geneticalgorithm.selection;

import io.github.geneticalgorithm.Epoch;
import io.github.geneticalgorithm.callback.Callback;
import io.github.geneticalgorithm.chromosome.Chromosome;

import java.util.Iterator;
import java.util.Map;

public interface SelectionStrategy<T> extends Iterator<Chromosome<T>>, Callback<T>
{
    /**
     * Initializes the population for the current epoch. The strategy can remove elements from this collection
     * @param population population for the current epoch
     */
    void setPopulation(Map<Chromosome<T>, Double> population);

    @Override
    boolean hasNext();

    @Override
    Chromosome<T> next();

    @Override
    default void onTrainingStart() {}

    @Override
    default void onTrainingFinish(Epoch<T> lastEpoch) {}

    @Override
    default void onEpochStart(int epochNum) {}

    @Override
    default void onEpochFinish(Epoch<T> epoch) {}
}
