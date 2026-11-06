package io.github.geneticalgorithm.gui.population;

import java.util.Arrays;
import java.util.stream.IntStream;

public class NoOutliersMinMaxBrightnessStrategy implements BrightnessStrategy
{
    private final double zscoreOutliers;

    public NoOutliersMinMaxBrightnessStrategy(double zscoreOutliers)
    {
        this.zscoreOutliers = zscoreOutliers;
    }

    @Override
    public double getBrightness(double[] fitnesses, int i)
    {
        double mean = Arrays.stream(fitnesses).average().orElse(0);

        double std = Math.sqrt(Arrays.stream(fitnesses).map(f -> Math.pow((f - mean), 2)).sum() / fitnesses.length);

        double[] zscores = Arrays.stream(fitnesses)
                .map(f -> (f - mean) / std)
                .toArray();

        double min = IntStream.range(0, fitnesses.length)
                .filter(j -> Math.abs(zscores[j]) <= zscoreOutliers)
                .mapToDouble(j -> fitnesses[j])
                .min()
                .orElse(Double.MIN_VALUE);
        double max = Arrays.stream(fitnesses)
                .max()
                .orElse(Double.MAX_VALUE);


        double scaled = (fitnesses[i] - min) / (max - min);

        return Math.max(0, Math.min(1, scaled));
    }
}
