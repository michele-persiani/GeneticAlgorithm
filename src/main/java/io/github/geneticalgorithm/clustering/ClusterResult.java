package io.github.geneticalgorithm.clustering;

import io.github.geneticalgorithm.chromosome.Chromosome;
import io.github.geneticalgorithm.distance.DistanceFunction;

import java.util.Collection;
import java.util.Collections;

public class ClusterResult<T>
{
    private final DistanceFunction<T> distanceFunction;
    private final Collection<Cluster<T>> clusters;

    public ClusterResult(Collection<Cluster<T>> clusters, DistanceFunction<T> distanceFunction)
    {
        this.clusters = clusters;
        this.distanceFunction = distanceFunction;
    }

    public Collection<Cluster<T>> getClusters()
    {
        return Collections.unmodifiableCollection(clusters);
    }

    public double getAverageIntraclusterDistance()
    {
        int n = 0;
        double totalDistance = 0;
        for(Cluster<T> cluster : clusters)
        {
            for(Chromosome<T> ch0 : cluster.getElements())
            {
                for(Chromosome<T> ch1 : cluster.getElements())
                {
                    double distance = distanceFunction.computeDistance(ch0, ch1);
                    totalDistance += distance;
                    n += 1;
                }
            }
        }
        return totalDistance / n;
    }

    public double getAverageInterCentroidDistance()
    {
        double totalDistance = 0;
        for(Cluster<T> c0 : clusters)
        {
            for(Cluster<T> c1 : clusters)
            {
                double centroidDistance = distanceFunction.computeDistance(c0.getCentroid(), c1.getCentroid());
                totalDistance += centroidDistance;
            }
        }
        return totalDistance / Math.pow(clusters.size(), 2);
    }
}
