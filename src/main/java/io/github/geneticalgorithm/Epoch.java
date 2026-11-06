package io.github.geneticalgorithm;

import io.github.geneticalgorithm.chromosome.Chromosome;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Epoch<T>
{
    private final Map<Chromosome<T>, Double> population;
    private final int epochNum;

    protected Epoch(int epochNum, Map<Chromosome<T>, Double> population)
    {
        this.population = new LinkedHashMap<>(population);
        this.epochNum = epochNum;
    }

    public int getEpochNum()
    {
        return epochNum;
    }

    public Map<Chromosome<T>, Double> getPopulation()
    {
        return Collections.unmodifiableMap(population);
    }

    public Chromosome<T> getBestGene()
    {
        return population.keySet()
                .stream()
                .max(Comparator.comparing(population::get))
                .orElseThrow();
    }
}
