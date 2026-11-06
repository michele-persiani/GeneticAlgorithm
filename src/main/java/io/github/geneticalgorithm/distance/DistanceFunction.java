package io.github.geneticalgorithm.distance;

import io.github.geneticalgorithm.chromosome.Chromosome;

public interface DistanceFunction<T>
{
    double computeDistance(Chromosome<T> first, Chromosome<T> second);
}
