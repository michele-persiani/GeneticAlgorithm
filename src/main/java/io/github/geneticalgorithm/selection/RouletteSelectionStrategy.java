package io.github.geneticalgorithm.selection;

import io.github.geneticalgorithm.chromosome.Chromosome;

import java.util.Map;
import java.util.stream.Collectors;

public class RouletteSelectionStrategy<T> extends SamplingBasedSelectionStrategy<T>
{

    private double normalizationBeta = 1e-1;

    public void setBetaExp(double betaExp)
    {
        this.normalizationBeta = betaExp;
    }

    @Override
    public Chromosome<T> sample()
    {
        Map<Chromosome<T>, Double> normalizedPopulation = population.keySet()
                .stream()
                .collect(Collectors.toMap(
                        k -> k,
                        k -> Math.exp(normalizationBeta * population.get(k))
                ));

        double sum = normalizedPopulation.values().stream().mapToDouble(v -> v).sum();
        normalizedPopulation.keySet().forEach(ch -> normalizedPopulation.put(ch, normalizedPopulation.get(ch) / sum));


        double random = Math.random();
        double current = 0;
        for(Chromosome<T> chromosome : normalizedPopulation.keySet())
        {
            current += normalizedPopulation.get(chromosome);
            if(current >= random)
                return chromosome;
        }
        throw new IllegalStateException("Should not happen");
    }
}
