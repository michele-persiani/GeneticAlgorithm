package io.github.geneticalgorithm.endcondition;


import io.github.geneticalgorithm.Epoch;

import java.util.Arrays;

public interface EndCondition<T>
{
    boolean isTrainingFinished(Epoch<T> epoch);
}
