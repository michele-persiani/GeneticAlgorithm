package io.github.geneticalgorithm.chromosome;

import java.util.Arrays;

public abstract class BooleanChromosome extends ArrayChromosome<Boolean>
{
    public BooleanChromosome(Boolean[] genes)
    {
        super(genes);
    }

    public BooleanChromosome(int size)
    {
        this(new Boolean[size]);
        randomize();
    }

    @Override
    public void mutate(int position)
    {
        setGene(position, !getGene(position));
    }

    @Override
    public void randomize(int position)
    {
        setGene(position, Math.random() < 0.5);
    }


    @Override
    public String toString()
    {
        return Arrays.stream(genes)
                .map(v -> v ? "1" : "0")
                .reduce("", (a, b) -> a + b);
    }
}
