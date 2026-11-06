package io.github.geneticalgorithm.gui.population;

import java.util.Arrays;

public class MinMaxBrightnessStrategy implements BrightnessStrategy
{
    @Override
    public double getBrightness(double[] fitnesses, int i)
    {
        double min = Arrays.stream(fitnesses).min().orElse(Double.MIN_VALUE);
        double max = Arrays.stream(fitnesses).max().orElse(Double.MAX_VALUE);
        return (fitnesses[i] - min) / (max - min);
    }
}
