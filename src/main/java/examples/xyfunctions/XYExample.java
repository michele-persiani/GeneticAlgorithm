package examples.xyfunctions;

import io.github.geneticalgorithm.GeneticAlgorithm;
import io.github.geneticalgorithm.endcondition.EndConditionFactory;
import io.github.geneticalgorithm.gui.FitnessPlotter;
import io.github.geneticalgorithm.gui.population.NoOutliersMinMaxBrightnessStrategy;
import io.github.geneticalgorithm.gui.population.PopulationPlotter;



public class XYExample
{
    public static void main(String[] args)
    {
        GeneticAlgorithm<Double> ga = new GeneticAlgorithm<>();
        ga.setChromosomePrototype(new XYChromosome());
        ga.setEndCondition(new EndConditionFactory().createEpochNumReached(100));

        PopulationPlotter<Double> plotter = new PopulationPlotter<>(ch -> {
            double[] v = new double[2];
            v[0] = ch.getGene(0);
            v[1] = ch.getGene(1);
            return v;
        });
        plotter.setBrightnessStrategy(new NoOutliersMinMaxBrightnessStrategy(3));

        ga.addCallback(plotter);

        FitnessPlotter<Double> fitnessPlotter = new FitnessPlotter<>();
        ga.addCallback(fitnessPlotter);

        ga.run();
    }
}
