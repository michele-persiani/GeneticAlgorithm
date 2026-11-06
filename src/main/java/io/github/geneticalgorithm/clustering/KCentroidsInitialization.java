package io.github.geneticalgorithm.clustering;

import io.github.geneticalgorithm.chromosome.Chromosome;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class KCentroidsInitialization<T> implements CentroidsInitialization<T>
{
    private final int k;

    public KCentroidsInitialization(int k)
    {
        this.k = k;
    }

    @Override
    public Collection<Chromosome<T>> getInitialCentroids(Collection<Chromosome<T>> population)
    {
        if(population.size() < k)
            throw new IllegalArgumentException("Data size is less than number of centroids");
        List<Chromosome<T>> shuffled = new ArrayList<>(population);
        Collections.shuffle(shuffled);
        return shuffled.subList(0, k);
    }
}
