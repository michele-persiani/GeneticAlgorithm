package io.github.geneticalgorithm.selection;

import io.github.geneticalgorithm.chromosome.Chromosome;

import java.util.*;

public abstract class SamplingBasedSelectionStrategy<T> implements SelectionStrategy<T>
{
    private boolean replacement = true;
    protected Map<Chromosome<T>, Double> population;

    public void setReplacement(boolean replacement)
    {
        this.replacement = replacement;
    }

    @Override
    public void setPopulation(Map<Chromosome<T>, Double> population)
    {
        this.population = new LinkedHashMap<>(population);
    }

    @Override
    public boolean hasNext()
    {
        return !population.isEmpty();
    }

    @Override
    public Chromosome<T> next()
    {
        if(population == null || population.isEmpty())
            throw new NoSuchElementException();
        Chromosome<T> sample = sample();
        if(!replacement)
            population.remove(sample);
        return sample;
    }

    protected abstract Chromosome<T> sample();
}
