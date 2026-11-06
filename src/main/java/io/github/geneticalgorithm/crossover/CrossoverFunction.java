package io.github.geneticalgorithm.crossover;

import io.github.geneticalgorithm.Epoch;
import io.github.geneticalgorithm.callback.Callback;
import io.github.geneticalgorithm.chromosome.Chromosome;

import java.util.Collection;

public interface CrossoverFunction<T> extends Callback<T>
{
    Collection<Chromosome<T>> crossover(Chromosome<T> first, Chromosome<T> second);

    @Override
    default void onTrainingStart() {}

    @Override
    default void onTrainingFinish(Epoch<T> lastEpoch) {}

    @Override
    default void onEpochStart(int epochNum) {}

    @Override
    default void onEpochFinish(Epoch<T> epoch) {}
}