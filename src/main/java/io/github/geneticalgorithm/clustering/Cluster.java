package io.github.geneticalgorithm.clustering;

import io.github.geneticalgorithm.chromosome.Chromosome;

import java.util.Collection;

public class Cluster<T>
{
    private final Chromosome<T> centroid;

    private final Collection<Chromosome<T>> elements;

    public Cluster(Chromosome<T> centroid, Collection<Chromosome<T>> elements)
    {
        this.centroid = centroid;
        this.elements = elements;
    }

    public Chromosome<T> getCentroid()
    {
        return centroid;
    }

    public Collection<Chromosome<T>> getElements()
    {
        return elements;
    }

    public boolean contains(Chromosome<T> element)
    {
        return elements.contains(element);
    }
}
