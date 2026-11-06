package io.github.geneticalgorithm.selection;

import io.github.geneticalgorithm.chromosome.Chromosome;

import java.util.*;

public class TournamentSelection<T> extends SamplingBasedSelectionStrategy<T>
{
    private final int tournamentSize;

    public TournamentSelection(int tournamentSize)
    {
        this.tournamentSize = tournamentSize;
    }

    @Override
    protected Chromosome<T> sample()
    {
        List<Chromosome<T>> populationList = new ArrayList<>(population.keySet());
        Collections.shuffle(populationList);
        return populationList
                .stream()
                .limit(tournamentSize)
                .max(Comparator.comparing(v -> population.get(v)))
                .orElseThrow(NoSuchElementException::new);
    }
}
