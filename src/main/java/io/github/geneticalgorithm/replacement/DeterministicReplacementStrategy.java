package io.github.geneticalgorithm.replacement;

import io.github.geneticalgorithm.chromosome.Chromosome;
import io.github.geneticalgorithm.distance.DistanceFunction;


import java.util.*;

public class DeterministicReplacementStrategy<T> implements ReplacementStrategy<T>
{
    private final DistanceFunction<T> distanceFunction;

    public DeterministicReplacementStrategy(DistanceFunction<T> distanceFunction)
    {
        this.distanceFunction = distanceFunction;
    }

    @Override
    public void replace(Map<Chromosome<T>, Double> population, Map<Chromosome<T>, Double> offspring)
    {
        HashMap<Chromosome<T>, Double> newPopulation = new LinkedHashMap<>();


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

            double childFitness = offspring.get(closestChild);
            double parentFitness = population.get(parent);

            if(childFitness <= parentFitness)
                newPopulation.put(parent, parentFitness);
            else
            {
                newPopulation.put(closestChild, childFitness);
                offspring.remove(closestChild);
            }

        }
        population.clear();
        population.putAll(newPopulation);
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
