package io.github.geneticalgorithm.selection;

import io.github.geneticalgorithm.chromosome.Chromosome;


import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Strategy returning the same population randomly shuffled. No selection based on fitness is performed
 * @param <T>
 */
public class SamePopulationStrategy<T> implements SelectionStrategy<T>
{
    private final Map<Chromosome<T>, Double> population = new LinkedHashMap<>();

    @Override
    public void setPopulation(Map<Chromosome<T>, Double> population)
    {
        this.population.clear();
        this.population.putAll(population);
    }

    @Override
    public boolean hasNext()
    {
        return !population.isEmpty();
    }

    @Override
    public Chromosome<T> next()
    {
        int skipped = (int) (Math.random() * population.size());
        return population.keySet()
                .stream()
                .skip(skipped)
                .findFirst()
                .orElseThrow();
    }
}
