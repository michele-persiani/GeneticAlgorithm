package io.github.geneticalgorithm.clustering;

import io.github.geneticalgorithm.chromosome.Chromosome;
import io.github.geneticalgorithm.distance.DistanceFunction;

import java.util.*;

public class KMeans<T>
{

    private CentroidsInitialization<T> centroidsInitialization = null;

    private final DistanceFunction<T> distanceFunction;

    private int maxIterations = 200;

    private double tolerance = 1e-3;

    public KMeans(DistanceFunction<T> distanceFunction)
    {
        this.distanceFunction = distanceFunction;
    }

    public void setMaxIterations(int maxIterations)
    {
        this.maxIterations = maxIterations;
    }

    public void setTolerance(double tolerance)
    {
        this.tolerance = tolerance;
    }

    /**
     * Perform k-means clustering on a list of items.
     *
     * @param items The list of items to cluster.
     * @return A list of clusters, where each cluster is a list of items.
     */
    public ClusterResult<T> cluster(Collection<Chromosome<T>> items)
    {
        // Initialize centroids randomly
        List<Chromosome<T>> centroids = initializeCentroids(items);

        // K-means clustering loop
        List<Collection<Chromosome<T>>> clusters = null;
        for (int iteration = 0; iteration < maxIterations; iteration++)
        {
            // Assign items to the nearest centroid
            clusters = assignClusters(items, centroids);

            // Calculate new centroids
            List<Chromosome<T>> newCentroids = calculateCentroids(clusters);

            // If the centroids do not change, we can stop
            if (hasConverged(centroids, newCentroids))
                break;

            centroids = newCentroids; // Update centroids for the next iteration
        }

        List<Cluster<T>> result = new ArrayList<>();
        for(int i = 0; i < centroids.size(); i++)
        {
            Cluster<T> cl = new Cluster<>(centroids.get(i), clusters.get(i));
            result.add(cl);
        }
        return new ClusterResult<>(result, distanceFunction);
    }


    public void setCentroidsInitialization(CentroidsInitialization<T> centroidsInitialization)
    {
        this.centroidsInitialization = centroidsInitialization;
    }

    private List<Chromosome<T>> initializeCentroids(Collection<Chromosome<T>> items)
    {
        return new ArrayList<>(centroidsInitialization.getInitialCentroids(items));
    }

    private List<Collection<Chromosome<T>>> assignClusters(Collection<Chromosome<T>> items, List<Chromosome<T>> centroids)
    {
        List<Collection<Chromosome<T>>> clusters = new ArrayList<>();

        // Initialize empty clusters
        for (Chromosome<T> chromosome : centroids)
        {
            HashSet<Chromosome<T>> cluster = new HashSet<>();
            cluster.add(chromosome);
            clusters.add(cluster);
        }


        // Assign each item to the nearest centroid
        for (Chromosome<T> item : items)
        {
            int closestCentroidIndex = 0;
            double smallestDistance = Double.MAX_VALUE;

            for (int j = 0; j < centroids.size(); j++)
            {
                Chromosome<T> centroid = centroids.get(j);
                double distance = distanceFunction.computeDistance(item, centroid);
                if (distance <= smallestDistance)
                {
                    smallestDistance = distance;
                    closestCentroidIndex = j;
                }
            }

            clusters.get(closestCentroidIndex).add(item);
        }

        return clusters;
    }

    private List<Chromosome<T>> calculateCentroids(List<Collection<Chromosome<T>>> clusters)
    {
        List<Chromosome<T>> centroids = new ArrayList<>();
        for(Collection<Chromosome<T>> cluster : clusters)
        {
            if(cluster.isEmpty())
                throw new IllegalStateException();
            Map<Chromosome<T>, Double> interclusterDistance = new HashMap<>();

            for(Chromosome<T> item : cluster)
                interclusterDistance.put(item, 0.0);

            for(Chromosome<T> first : cluster)
                for(Chromosome<T> second : cluster)
                    interclusterDistance.put(first, interclusterDistance.get(first) + distanceFunction.computeDistance(first, second));

            for(Chromosome<T> item : cluster)
                interclusterDistance.put(item, Math.pow(interclusterDistance.get(item), 2));


            double minInterclusterDistance = Double.MAX_VALUE;
            Chromosome<T> centroid = null;
            for(Chromosome<T> item : cluster)
            {
                double itemInterclusterDistance = interclusterDistance.get(item);
                if(itemInterclusterDistance < minInterclusterDistance)
                {
                    minInterclusterDistance = itemInterclusterDistance;
                    centroid = item;
                }
            }
            centroids.add(centroid);

        }
        return centroids;
    }

    private boolean hasConverged(List<Chromosome<T>> oldCentroids, List<Chromosome<T>> newCentroids)
    {
        if(oldCentroids.size() != newCentroids.size())
            return false;

        for (int i = 0; i < oldCentroids.size(); i++)
            if (distanceFunction.computeDistance(oldCentroids.get(i), newCentroids.get(i)) > tolerance)
                return false;
        return true;
    }
}
