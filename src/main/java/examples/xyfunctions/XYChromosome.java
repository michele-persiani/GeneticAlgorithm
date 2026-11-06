package examples.xyfunctions;

import io.github.geneticalgorithm.chromosome.Chromosome;
import io.github.geneticalgorithm.chromosome.DoubleChromosome;

public class XYChromosome extends DoubleChromosome
{
    private final DistanceBasedFitness fitness;

    public XYChromosome()
    {
        super(new Double[]{0., 0.}, -100, 100, 5);
        fitness = new DistanceBasedFitness()
                .setBaseFitness(100)
                .setMinDistance(10);
        for(int i = -100; i <= 100; i+=40)
            for(int j = -100; j <= 100; j+=40)
                fitness.addCenter(i, j);
    }

    @Override
    public Chromosome<Double> clone()
    {
        XYChromosome clone = new XYChromosome();
        clone.setGene(0, getGene(0));
        clone.setGene(1, getGene(1));
        return clone;
    }


    @Override
    public double computeFitness()
    {
        return fitness.computeFitness(getGene(0), getGene(1));
    }
}
