package io.github.geneticalgorithm.callback;

import io.github.geneticalgorithm.Epoch;
import io.github.lightdataframe.lightweightdataframe.Dataframe;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FitnessStatisticsCallback<T> implements Callback<T>
{
    private Dataframe df;

    public Dataframe getStatisticsDataframe()
    {
        return df;
    }

    @Override
    public void onTrainingStart()
    {
        df = new Dataframe();
    }

    @Override
    public void onEpochStart(int iteration)
    {

    }

    @Override
    public void onEpochFinish(Epoch<T> epoch)
    {
        Dataframe fitnessDataframe = epoch.getPopulation()
                .values()
                .stream()
                .collect(
                        Dataframe.<Double>collector()
                                .addColumn("Fitness", v -> v)
                );

        fitnessDataframe.statistics()
                .getRows()  // Only one row should be present
                .forEach(row -> {
                    Map<String, Double> values = new LinkedHashMap<>();
                    values.put("Epoch", (double) epoch.getEpochNum());
                    values.putAll(row);
                    df.append(true, values);
                });

        fitnessDataframe.printStatistics();
    }

    @Override
    public void onTrainingFinish(Epoch<T> lastEpoch)
    {

    }
}
