package io.github.geneticalgorithm.replacement;

import io.github.geneticalgorithm.chromosome.Chromosome;
import io.github.geneticalgorithm.distance.DistanceFunction;

import java.util.*;

/**
 * Generalized crowding from "Severino F. Galan, Ole J. Mengshoel Generalized Crowding for Genetic Algorithms"
 *
 */
public class GeneralizedReplacementStrategy<T> implements ReplacementStrategy<T>
{
    private final DistanceFunction<T> distanceFunction;

    private double initialScalingFactor = 1;
    private double scalingFactor = 1;
    private double annealingFactor = 1;

    public GeneralizedReplacementStrategy(DistanceFunction<T> distanceFunction)
    {
        this.distanceFunction = distanceFunction;
    }

    /**
     * Annealing factor for the scaling factor. At the call of {@code replace()} the scaling factor is multiplied by
     * this value
     * @param scalingFactorMul a value between 0 and 1
     */
    public void setAnnealing(double scalingFactorMul)
    {
        this.annealingFactor = Math.max(0, Math.min(1, scalingFactorMul));
    }

    /**
     *
     * @param scalingFactor a value greater than 0
     */
    public void setScalingFactor(double scalingFactor)
    {
        this.initialScalingFactor = Math.max(0, scalingFactor);
    }

    public void setDeterministic()
    {
        initialScalingFactor = 0;
    }

    public void setProbabilistic()
    {
        initialScalingFactor = 1;
    }

    @Override
    public void onTrainingStart()
    {
        ReplacementStrategy.super.onTrainingStart();
        scalingFactor = initialScalingFactor;
    }

    @Override
    public void replace(Map<Chromosome<T>, Double> population, Map<Chromosome<T>, Double> offspring)
    {
        Map<Chromosome<T>, Double> newPopulation = new LinkedHashMap<>();

        for(Chromosome<T> parent : population.keySet())
        {
            Comparator<Chromosome<T>> comparator = new CachedDistanceComparator<>(parent, distanceFunction);
            Chromosome<T> closestChild = offspring.keySet()
                    .stream()
                    .filter(c -> !newPopulation.containsKey(c))
                    .min(comparator)
                    .orElse(null);

            if(closestChild == null)
                break;

            double fc = offspring.get(closestChild);
            double fp = population.get(parent);
            double pc = 0.5;

            if (fc > fp)
                pc = fc / (fc + scalingFactor * fp);
            if(fc < fp)
                pc = scalingFactor * fc / (scalingFactor * fc + fp);

            if(pc < 0)
                throw new IllegalStateException();

            boolean childReplacesParent = Math.random() <= pc;

            if(childReplacesParent)
                newPopulation.put(closestChild, fc);
            else
                newPopulation.put(parent, fp);

        }
        population.clear();
        population.putAll(newPopulation);

        scalingFactor *= annealingFactor;
    }

    private static class CachedDistanceComparator<T> implements Comparator<Chromosome<T>>
    {
        private final Chromosome<T> target;
        private final DistanceFunction<T> distanceFunction;
        private final Map<Chromosome<T>, Double> cache = new HashMap<>();


        public CachedDistanceComparator(Chromosome<T> target, DistanceFunction<T> distanceFunction)
        {
            this.target = target;
            this.distanceFunction = distanceFunction;
        }


        @Override
        public int compare(Chromosome<T> o1, Chromosome<T> o2)
        {
            if(!cache.containsKey(o1))
                cache.put(o1, distanceFunction.computeDistance(o1, target));
            if(!cache.containsKey(o2))
                cache.put(o2, distanceFunction.computeDistance(o2, target));

            return (int) Math.signum(cache.get(o1) - cache.get(o2));
        }
    }
}
