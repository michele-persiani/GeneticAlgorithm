package io.github.geneticalgorithm.crossover;

import io.github.geneticalgorithm.chromosome.Chromosome;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class KPointCrossover<T> implements CrossoverFunction<T>
{
    private final int k;

    private final Random random = new Random();

    public KPointCrossover(int k)
    {
        this.k = k;
    }

    @Override
    public Collection<Chromosome<T>> crossover(Chromosome<T> first, Chromosome<T> second)
    {
        Chromosome<T> child0 = first.clone();
        Chromosome<T> child1 = second.clone();

        int len = first.size();
        int[] cp = IntStream.range(0, k + 1)
                .map(i -> i == 0 ? 0 : random.nextInt(len))
                .sorted()
                .toArray();


        boolean swapped = random.nextBoolean();
        for(int i = 0; i < cp.length - 1; i++)
        {
            swapped = !swapped;
            if(!swapped)
                continue;
            int min = cp[i];
            int max = cp[i + 1];

            for(int j = min; j < max; j++)
            {
                T tmp = child0.getGene(j);
                child0.setGene(j, child1.getGene(j));
                child1.setGene(j, tmp);
            }
        }

        return List.of(child0, child1);
    }
}
