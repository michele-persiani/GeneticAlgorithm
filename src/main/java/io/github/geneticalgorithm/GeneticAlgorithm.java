package io.github.geneticalgorithm;

import io.github.geneticalgorithm.callback.Callback;
import io.github.geneticalgorithm.chromosome.Chromosome;
import io.github.geneticalgorithm.crossover.CrossoverFunction;
import io.github.geneticalgorithm.crossover.SinglePointCrossover;
import io.github.geneticalgorithm.endcondition.EndCondition;
import io.github.geneticalgorithm.replacement.ReplaceAllPopulationStrategy;
import io.github.geneticalgorithm.replacement.ReplacementStrategy;
import io.github.geneticalgorithm.selection.SelectionStrategy;
import io.github.geneticalgorithm.selection.TournamentSelection;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class GeneticAlgorithm<T>
{
    private EndCondition<T> endCondition = epoch -> epoch.getEpochNum() >= 100;

    private SelectionStrategy<T> selectionStrategy = new TournamentSelection<>(10);

    private Chromosome<T> chromosomePrototype = null;

    private CrossoverFunction<T> crossoverFunction = new SinglePointCrossover<>();

    private ReplacementStrategy<T> replacementStrategy = new ReplaceAllPopulationStrategy<>();

    private Executor executor = Executors.newFixedThreadPool(10);

    private double mutationProbability = 0.05;

    private int populationSize = 100;

    // Changed during execution
    private final List<Epoch<T>> epochs = new ArrayList<>();

    private final List<Callback<T>> callbacks = new ArrayList<>();
    private int keepBestN = 5;

    public void setChromosomePrototype(Chromosome<T> chromosomePrototype)
    {
        this.chromosomePrototype = chromosomePrototype;
    }

    public void setSelectionStrategy(SelectionStrategy<T> selectionStrategy)
    {
        this.selectionStrategy = selectionStrategy;
    }

    public void setCrossoverFunction(CrossoverFunction<T> crossoverFunction)
    {
        this.crossoverFunction = crossoverFunction;
    }

    public void setReplacementStrategy(ReplacementStrategy<T> replacementStrategy)
    {
        this.replacementStrategy = replacementStrategy;
    }

    public void setExecutor(Executor executor)
    {
        this.executor = executor;
    }

    public void setEndCondition(EndCondition<T> endCondition)
    {
        this.endCondition = endCondition;
    }

    public void addCallback(Callback<T> callback)
    {
        this.callbacks.add(callback);
    }

    public void setPopulationSize(int populationSize)
    {
        this.populationSize = populationSize;
    }

    public void setMutationProbability(double mutationProbability)
    {
        this.mutationProbability = mutationProbability;
    }

    public void setKeepBestN(int keepBestN)
    {
        this.keepBestN = keepBestN;
    }

    public synchronized void run()
    {
        Collection<Chromosome<T>> population = new ArrayList<>();
        Collection<Chromosome<T>> offspring = new ArrayList<>();
        Map<Chromosome<T>, Double> offspringFitness = new HashMap<>();
        Map<Chromosome<T>, Double> populationFitness = new HashMap<>();
        int iteration = 0;


        epochs.clear();

        callbacks.add(selectionStrategy);
        callbacks.add(crossoverFunction);
        callbacks.add(replacementStrategy);

        callbacks.forEach(Callback::onTrainingStart);

        initializePopulation(population);
        computeFitness(population, populationFitness);

        do
        {
            iteration++;
            offspring.clear();

            int epochNum = iteration;
            callbacks.forEach(callback -> callback.onEpochStart(epochNum));

            // Keep best individuals
            if(keepBestN > 0)
            {
                populationFitness.keySet()
                        .stream()
                        .sorted(Comparator.comparing(ch -> - populationFitness.get(ch)))
                        .limit(keepBestN)
                        .map(Chromosome::clone)
                        .forEach(offspring::add);
            }

            // NB. the selection strategy can remove chromosomes from the population
            selectionStrategy.setPopulation(new HashMap<>(populationFitness));

            while(selectionStrategy.hasNext() && offspring.size() < populationSize)
            {
                // Selection
                Chromosome<T> chromosome0 = selectionStrategy.next();
                Chromosome<T> chromosome1 = selectionStrategy.hasNext() ? selectionStrategy.next() : chromosome0;

                //Crossover
                Collection<Chromosome<T>> children = crossoverFunction.crossover(chromosome0.clone(), chromosome1.clone());

                // Mutation
                for(Chromosome<T> child : children)
                {
                    if(offspring.size() >= populationSize)
                        break;
                    if (Math.random() <= mutationProbability)
                    {
                        int position = (int) (Math.random() * child.size());
                        child.mutate(position);
                    }
                    offspring.add(child);
                }
            }

            computeFitness(offspring, offspringFitness);

            replacementStrategy.replace(populationFitness, offspringFitness);

            epochs.add(new Epoch<>(iteration, populationFitness));

            callbacks.forEach(callback -> callback.onEpochFinish(getLastEpoch()));
        }
        while(!endCondition.isTrainingFinished(getLastEpoch()));

        callbacks.forEach(tCallback -> tCallback.onTrainingFinish(getLastEpoch()));

        callbacks.remove(selectionStrategy);
        callbacks.remove(crossoverFunction);
        callbacks.remove(replacementStrategy);
    }

    private void computeFitness(Collection<Chromosome<T>> population, Map<Chromosome<T>, Double> populationFitness)
    {
        AtomicInteger n = new AtomicInteger(population.size());
        Semaphore semaphore = new Semaphore(0);

        populationFitness.clear();

        for(Chromosome<T> ch : population)
            executor.execute(() ->
            {
                double fitness = ch.computeFitness();
                synchronized (semaphore)
                {
                    populationFitness.put(ch, fitness);
                    int remaining = n.decrementAndGet();
                    if (remaining == 0)
                        semaphore.release();
                }
            });

        try
        {
            semaphore.acquire();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void initializePopulation(Collection<Chromosome<T>> population)
    {
        if(chromosomePrototype == null)
            throw new IllegalStateException("Chromosome prototype is null");
        population.clear();
        while(population.size() < populationSize)
        {
            Chromosome<T> chromosome = chromosomePrototype.clone();
            chromosome.randomize();
            population.add(chromosome);
        }
    }

    public Epoch<T> getLastEpoch()
    {
        return epochs.isEmpty() ? null : epochs.get(epochs.size() - 1);
    }

    public List<Epoch<T>> getEpochs()
    {
        return Collections.unmodifiableList(epochs);
    }
}
