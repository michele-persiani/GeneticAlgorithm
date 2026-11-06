package io.github.geneticalgorithm.clustering;

import io.github.geneticalgorithm.chromosome.Chromosome;
import io.github.geneticalgorithm.distance.DistanceFunction;

import java.util.ArrayList;
import java.util.Collection;

public class ClosestCentroidsInitialization<T> implements CentroidsInitialization<T>
{
    private Collection<Chromosome<T>> centroids;
    private final DistanceFunction<T>  distanceFunction;

    public ClosestCentroidsInitialization(DistanceFunction<T> distanceFunction, Collection<Chromosome<T>> centroids)
    {
        this.distanceFunction = distanceFunction;
        this.centroids = centroids;
    }

    public void setCentroids(Collection<Chromosome<T>> centroids)
    {
        this.centroids = centroids;
    }

    @Override
    public Collection<Chromosome<T>> getInitialCentroids(Collection<Chromosome<T>> population)
    {
        Collection<Chromosome<T>> result = new ArrayList<>();

        for(Chromosome<T> centroid : centroids)
        {
            double bestDistance = Double.MAX_VALUE;
            Chromosome<T> bestMatch = null;
            for(Chromosome<T> chromosome : population)
            {
                double distance = distanceFunction.computeDistance(centroid, chromosome);
                if(distance < bestDistance && !result.contains(chromosome))
                {
                    bestDistance = distance;
                    bestMatch = chromosome;
                }
            }
            result.add(bestMatch);
        }

        return result;
    }
}
