package io.github.geneticalgorithm.distance;


import java.util.function.BiPredicate;

public class DistanceFunctionFactory
{
    public DistanceFunction<Double> createMinkowskiDistance(double p)
    {
        return (first, second) -> {

            double squaresSum = 0;
            for(int i = 0; i < first.size(); i++)
                squaresSum += Math.pow(Math.abs(first.getGene(i) - second.getGene(i)), p);
            return Math.pow(squaresSum, 1 / p);
        };
    }

    public DistanceFunction<Double> createEuclideanDistance()
    {
        return createMinkowskiDistance(2);
    }

    public DistanceFunction<Double> createManhattanDistance()
    {
        return createMinkowskiDistance(1);
    }

    public <T> DistanceFunction<T> createEditDistance(BiPredicate<T, T> geneEquality)
    {
        return (first, second) -> {
            double substitutions = 0;
            for(int i = 0; i < first.size(); i++)
                if(!geneEquality.test(first.getGene(i),second.getGene(i)))
                    substitutions += 1;
            return substitutions;
        };
    }
}
