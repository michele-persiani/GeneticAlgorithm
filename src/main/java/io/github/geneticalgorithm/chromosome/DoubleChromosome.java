package io.github.geneticalgorithm.chromosome;

import java.util.Arrays;

public abstract class DoubleChromosome extends ArrayChromosome<Double>
{
    protected final double minValue;
    protected final double maxValue;
    protected final double mutationMagnitude;

    public DoubleChromosome(Double[] genes, double min, double max, double mutationMagnitude)
    {
        super(genes);
        if(min > max)
            throw new IllegalArgumentException("min must be less than max");
        if(mutationMagnitude < 0)
            throw new IllegalArgumentException("mutation magnitude must be >= 0");
        this.minValue = min;
        this.maxValue = max;
        this.mutationMagnitude = mutationMagnitude;
    }

    @Override
    public void mutate(int position)
    {
        double currentValue = getGene(position);
        double newValue = currentValue + (2 * Math.random() - 1) * mutationMagnitude;
        newValue = Math.max(minValue, Math.min(newValue, maxValue));
        setGene(position, newValue);
    }

    @Override
    public void randomize(int position)
    {
        setGene(position, Math.random() * (maxValue - minValue) + minValue);
    }

}
