package io.github.geneticalgorithm.callback;

import io.github.geneticalgorithm.Epoch;
import io.github.lightdataframe.lightweightdataframe.Dataframe;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

public class DataframeCallback<T> implements Callback<T>
{
    private Dataframe df = new Dataframe();

    private final Map<String, ToDoubleFunction<Epoch<T>>> columns = new LinkedHashMap<>();

    private DataframeCallback<T> addColumn(String name, ToDoubleFunction<Epoch<T>> getter)
    {
        columns.put(name, getter);
        return this;
    }

    public Dataframe getDataframe()
    {
        return df;
    }

    @Override
    public void onTrainingStart()
    {
        df = new Dataframe();
    }

    @Override
    public void onEpochStart(int epochNum)
    {

    }

    @Override
    public void onEpochFinish(Epoch<T> epoch)
    {
        Map<String, Double> row = columns.keySet()
                .stream()
                .collect(Collectors.toMap(
                        k -> k,
                        k -> columns.get(k).applyAsDouble(epoch)
                ));
        df.append(true, row);
    }

    @Override
    public void onTrainingFinish(Epoch<T> lastEpoch)
    {

    }
}
