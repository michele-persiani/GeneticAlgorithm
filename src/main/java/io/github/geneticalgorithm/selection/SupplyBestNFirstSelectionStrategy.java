package io.github.geneticalgorithm.selection;

import io.github.geneticalgorithm.chromosome.Chromosome;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SupplyBestNFirstSelectionStrategy<T> implements SelectionStrategy<T>
{
    private final SelectionStrategy<T> strategy;
    private final int n;
    private List<Chromosome<T>> bestN;

    public SupplyBestNFirstSelectionStrategy(int n, SelectionStrategy<T> strategy)
    {
        this.n = n;
        this.strategy = strategy;
    }

    @Override
    public void setPopulation(Map<Chromosome<T>, Double> population)
    {
        bestN = population.keySet()
                .stream()
                .sorted(Comparator.comparing(ch -> -population.get(ch)))
                .limit(n)
                .collect(Collectors.toList());
        bestN.forEach(population::remove);
        strategy.setPopulation(population);
    }

    @Override
    public boolean hasNext()
    {
        return !bestN.isEmpty() || strategy.hasNext();
    }

    @Override
    public Chromosome<T> next()
    {
        if(!bestN.isEmpty())
            return bestN.remove(0);
        return strategy.next();
    }
}
