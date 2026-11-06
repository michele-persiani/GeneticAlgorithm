package io.github.geneticalgorithm.gui.population;

import io.github.geneticalgorithm.Epoch;
import io.github.geneticalgorithm.chromosome.Chromosome;

import java.util.Map;

public class EpochPopulationStrategy<T> implements PopulationStrategy<T>
{
    @Override
    public Map<Chromosome<T>, Double> getPlotPopulation(Epoch<T> epoch)
    {
        return epoch.getPopulation();
    }
}
