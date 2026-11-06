package io.github.geneticalgorithm.gui;

import io.github.geneticalgorithm.Epoch;
import io.github.lightdataframe.lightweightdataframe.Dataframe;

import java.util.*;
import java.util.stream.Collectors;

public class FitnessPlotter<T> extends LinePlotter<T>
{
    private boolean log = false;
    private double scale = 1;

    public FitnessPlotter()
    {
        super("Fitness");
    }

    public void setUseLog10(boolean useLog)
    {
        this.log = useLog;
    }

    public void setScaleFactor(double scale)
    {
        this.scale = scale;
    }

    @Override
    protected Map<String, Double> getPlotData(Epoch<T> epoch)
    {
        Dataframe fitnesses = epoch.getPopulation()
                .values()
                .stream()
                .collect(Dataframe.<Double>collector().addColumn("fitness", v -> log ? Math.log10(v * scale) : v * scale));
        return fitnesses.statistics().getRow(0)
                .entrySet()
                .stream()
                .filter(e -> Arrays.asList("min", "max", "avg").contains(e.getKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        HashMap::new
                ));
    }
}
