package io.github.geneticalgorithm.gui.population;

import java.util.Arrays;

public class PercentileMinMaxBrightnessStrategy implements BrightnessStrategy
{
    private final double percentile;

    public PercentileMinMaxBrightnessStrategy(double percentile)
    {
        this.percentile = percentile;
    }

    @Override
    public double getBrightness(double[] fitnesses, int i)
    {
        double min = Arrays.stream(fitnesses)
                .filter(f -> getPercentile(fitnesses, f) >= percentile)
                .min()
                .orElse(Double.MIN_VALUE);

        double max = Arrays.stream(fitnesses)
                .max()
                .orElse(Double.MAX_VALUE);

        double scaled = (fitnesses[i] - min) / (max - min);

        return Math.max(0, Math.min(1, scaled));
    }

    private double getPercentile(double[] values, double value)
    {
        return Arrays.stream(values).map(f -> f <= value ? 1 : 0).sum() / values.length;
    }
}
