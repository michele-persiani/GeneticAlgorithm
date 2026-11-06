package io.github.geneticalgorithm.selection;

import io.github.geneticalgorithm.Epoch;
import io.github.geneticalgorithm.chromosome.Chromosome;
import io.github.geneticalgorithm.clustering.*;
import io.github.geneticalgorithm.distance.DistanceFunction;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class NichingSelectionStrategy<T> implements SelectionStrategy<T>
{
    private final int k;
    private final KMeans<T> kmeans;
    private final Supplier<SelectionStrategy<T>> selectionStrategyFactory;
    private final DistanceFunction<T> distanceFunction;
    private Map<Cluster<T>, SelectionStrategy<T>> clusterStrategyMap;
    private Cluster<T> currentCluster = null;
    private boolean keepCentroids = true;

    public NichingSelectionStrategy(int k, DistanceFunction<T> distanceFunction, Supplier<SelectionStrategy<T>> selectionStrategyFactory)
    {
        this.distanceFunction = distanceFunction;
        this.kmeans = new KMeans<>(distanceFunction);
        this.k = k;
        this.selectionStrategyFactory = selectionStrategyFactory;
    }

    public void setKeepCentroids(boolean keepCentroids)
    {
        this.keepCentroids = keepCentroids;
    }

    @Override
    public void onTrainingStart()
    {
        SelectionStrategy.super.onTrainingStart();
        kmeans.setCentroidsInitialization(new KCentroidsInitialization<>(k));
    }

    @Override
    public void onTrainingFinish(Epoch<T> lastEpoch)
    {
        SelectionStrategy.super.onTrainingFinish(lastEpoch);
        ClusterResult<T> clusterResult = kmeans.cluster(lastEpoch.getPopulation().keySet());
        clusterStrategyMap = clusterResult
                .getClusters()
                .stream()
                .collect(Collectors.toMap(c -> c, c -> selectionStrategyFactory.get()));
    }

    @Override
    public void setPopulation(Map<Chromosome<T>, Double> population)
    {
        ClusterResult<T> clusterResult = kmeans.cluster(population.keySet());
        clusterStrategyMap = clusterResult
                .getClusters()
                .stream()
                .collect(Collectors.toMap(c -> c, c -> selectionStrategyFactory.get()));
        clusterStrategyMap.forEach((c, s) -> s.setPopulation(
                c.getElements()
                        .stream()
                        .collect(Collectors.toMap(
                                k -> k,
                                population::get
                        ))
        ));
        if(keepCentroids)
            kmeans.setCentroidsInitialization(
                    new ClosestCentroidsInitialization<>(
                            distanceFunction,
                            clusterStrategyMap.keySet().stream().map(Cluster::getCentroid).collect(Collectors.toList())
                    )
            );
        currentCluster = null;
    }

    @Override
    public boolean hasNext()
    {
        return clusterStrategyMap.values().stream().anyMatch(SelectionStrategy::hasNext);
    }

    @Override
    public Chromosome<T> next()
    {
        if(currentCluster != null)
        {
            SelectionStrategy<T> clusterStrategy = this.clusterStrategyMap.get(currentCluster);
            if(clusterStrategy.hasNext())
            {
                Chromosome<T> next = clusterStrategy.next();
                currentCluster = null;
                return next;
            }
            else
            {
                currentCluster = null;
                return next();
            }
        }

        List<Cluster<T>> clustersWithNext = clusterStrategyMap.keySet()
                .stream()
                .filter(c -> clusterStrategyMap.get(c).hasNext())
                .collect(Collectors.toList());

        if(clustersWithNext.isEmpty())
            throw new NoSuchElementException();

        Collections.shuffle(clustersWithNext);
        currentCluster = clustersWithNext.get(0);

        return clusterStrategyMap.get(currentCluster).next();
    }
}
