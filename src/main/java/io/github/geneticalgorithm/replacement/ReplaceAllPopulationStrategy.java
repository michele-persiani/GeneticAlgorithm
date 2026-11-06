package io.github.geneticalgorithm.replacement;

import io.github.geneticalgorithm.chromosome.Chromosome;

import java.util.Map;

public class ReplaceAllPopulationStrategy<T> implements ReplacementStrategy<T>
{
    @Override
    public void replace(Map<Chromosome<T>, Double> population, Map<Chromosome<T>, Double> offspring)
    {
        population.clear();
        population.putAll(offspring);
    }
}
