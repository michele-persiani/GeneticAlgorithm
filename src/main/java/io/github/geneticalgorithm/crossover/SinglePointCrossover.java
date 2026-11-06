package io.github.geneticalgorithm.crossover;

import io.github.geneticalgorithm.chromosome.Chromosome;

import java.util.Collection;
import java.util.List;

public class SinglePointCrossover<T> implements CrossoverFunction<T>
{
    @Override
    public Collection<Chromosome<T>> crossover(Chromosome<T> first, Chromosome<T> second)
    {
        int position = (int) (Math.random() * first.size());
        Chromosome<T> child0 = first.clone();
        Chromosome<T> child1 = second.clone();

        for(int i = 0; i < position; i++)
            child0.setGene(i, child1.getGene(i));
        for(int i = position; i < second.size(); i++)
            child1.setGene(i, child0.getGene(i));

        return List.of(child0, child1);
    }
}
