package io.github.geneticalgorithm.clustering;

import io.github.geneticalgorithm.chromosome.Chromosome;

import java.util.Collection;



public interface CentroidsInitialization<T>
{
    Collection<Chromosome<T>> getInitialCentroids(Collection<Chromosome<T>> population);
}
