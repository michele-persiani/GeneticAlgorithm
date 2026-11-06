package io.github.geneticalgorithm.gui.population;

import io.github.geneticalgorithm.Epoch;
import io.github.geneticalgorithm.chromosome.Chromosome;

import java.util.Map;

public interface PopulationStrategy<T>
{
    Map<Chromosome<T>, Double> getPlotPopulation(Epoch<T> epoch);
}
