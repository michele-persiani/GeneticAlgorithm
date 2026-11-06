package io.github.geneticalgorithm.crossover;

import io.github.geneticalgorithm.chromosome.Chromosome;

import java.util.Collection;
import java.util.List;

public class UniformCrossover<T> implements CrossoverFunction<T>
{
    private final double swapProbability;

    public UniformCrossover(double swapProbability)
    {
        this.swapProbability = swapProbability;
    }

    @Override
    public Collection<Chromosome<T>> crossover(Chromosome<T> first, Chromosome<T> second)
    {
        Chromosome<T> child0 = first.clone();
        Chromosome<T> child1 = second.clone();

        for(int i = 0; i < child0.size(); i++)
        {
            if (Math.random() < swapProbability)
            {
                T tmp = first.getGene(i);
                child0.setGene(i, child1.getGene(i));
                child1.setGene(i, tmp);
            }
        }

        return List.of(child0, child1);
    }
}
