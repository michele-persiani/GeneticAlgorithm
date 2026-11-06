package io.github.geneticalgorithm.gui.population;

import io.github.geneticalgorithm.Epoch;
import io.github.geneticalgorithm.chromosome.Chromosome;

import java.util.*;

public class CachedPopulationStrategy<T> implements PopulationStrategy<T>
{
    private final int cacheSize;
    private final List<Chromosome<T>> cache = new ArrayList<>();
    private final Map<Chromosome<T>, Double> plotPopulation = new HashMap<>();

    public CachedPopulationStrategy(int cacheSize)
    {
        this.cacheSize = cacheSize;
    }

    @Override
    public Map<Chromosome<T>, Double> getPlotPopulation(Epoch<T> epoch)
    {
        plotPopulation.putAll(epoch.getPopulation());
        cache.addAll(epoch.getPopulation().keySet());

        while(cache.size() > cacheSize)
        {
            Chromosome<T> removed = cache.remove(0);
            plotPopulation.remove(removed);
        }

        return plotPopulation;
    }
}
