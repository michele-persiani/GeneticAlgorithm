package examples.booleans;

import io.github.geneticalgorithm.chromosome.BooleanChromosome;
import io.github.geneticalgorithm.chromosome.Chromosome;

public class BooleanWithFitnessChromosome extends BooleanChromosome
{
    public BooleanWithFitnessChromosome(Boolean[] genes)
    {
        super(genes);
    }

    public BooleanWithFitnessChromosome(int size)
    {
        super(size);
    }

    @Override
    public Chromosome<Boolean> clone()
    {
        return new BooleanWithFitnessChromosome(genes.clone());
    }

    @Override
    public double computeFitness()
    {
        double fitness = 0;
        for(int i = 0; i < size(); i++)
        {
            if (i < 40)
                fitness += !getGene(i) ? 1 : 0;
            if (i >= 40 && i < 60)
                fitness += getGene(i) ? 1 : 0;
            if (i >= 60)
                fitness += !getGene(i) ? 1 : 0;
        }
        return fitness;
    }
}
