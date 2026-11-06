package examples.booleans;

import io.github.geneticalgorithm.GeneticAlgorithm;
import io.github.geneticalgorithm.callback.FitnessStatisticsCallback;
import io.github.geneticalgorithm.chromosome.Chromosome;
import io.github.geneticalgorithm.crossover.KPointCrossover;
import io.github.geneticalgorithm.endcondition.EndConditionFactory;
import io.github.geneticalgorithm.gui.FitnessPlotter;
import io.github.geneticalgorithm.gui.population.CachedPopulationStrategy;
import io.github.geneticalgorithm.gui.population.NoOutliersMinMaxBrightnessStrategy;
import io.github.geneticalgorithm.gui.population.PopulationPlotter;
import io.github.geneticalgorithm.selection.TournamentSelection;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main
{
    public static void main(String[] args)
    {
        GeneticAlgorithm<Boolean> ga = new GeneticAlgorithm<>();

        EndConditionFactory endConditionFactory = new EndConditionFactory();

        ga.setCrossoverFunction(new KPointCrossover<>(5));
        ga.setChromosomePrototype(new BooleanWithFitnessChromosome(100));
        ga.setEndCondition(endConditionFactory.createEpochNumReached(300));
        ga.setSelectionStrategy(new TournamentSelection<>(10));
        ga.setPopulationSize(500);
        ga.setKeepBestN(5);

        ga.addCallback(new FitnessPlotter<>());

        PopulationPlotter<Boolean> populationPlotter = new PopulationPlotter<>(Main::chromosomeToArray);
        populationPlotter.setBrightnessStrategy(new NoOutliersMinMaxBrightnessStrategy(1));
        populationPlotter.setPopulationStrategy(new CachedPopulationStrategy<>(5000));
        ga.addCallback(populationPlotter);

        ga.run();

    }

    private static double[] chromosomeToArray(Chromosome<Boolean> ch)
    {
        double[] values = new double[ch.size()];
        for(int i = 0; i < ch.size(); i++)
            values[i] = ch.getGene(i) ? 1 : 0;
        return values;
    }
}