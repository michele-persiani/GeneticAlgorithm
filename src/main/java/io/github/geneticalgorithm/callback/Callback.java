package io.github.geneticalgorithm.callback;

import io.github.geneticalgorithm.Epoch;

public interface Callback<T>
{
    void onTrainingStart();

    void onEpochStart(int epochNum);

    void onEpochFinish(Epoch<T> epoch);

    void onTrainingFinish(Epoch<T> lastEpoch);
}
