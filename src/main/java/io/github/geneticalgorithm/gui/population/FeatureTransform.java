package io.github.geneticalgorithm.gui.population;

import io.github.geneticalgorithm.chromosome.Chromosome;

public interface FeatureTransform<T>
{
    double[] transform(Chromosome<T> chromosome);
}
