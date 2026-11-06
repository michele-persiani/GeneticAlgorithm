package io.github.geneticalgorithm.chromosome;

import java.util.Arrays;


public abstract class ArrayChromosome<T> implements Chromosome<T>
{
    protected final T[] genes;

    public ArrayChromosome(T[] genes)
    {
        this.genes = genes;
    }

    @Override
    public T getGene(int position)
    {
        return genes[position];
    }

    @Override
    public void setGene(int position, T gene)
    {
        genes[position] = gene;
    }

    /**
     * Returns a shallow copy of the genes array
     *
     * @return a shallow copy of the genes array
     */
    public T[] getGenes()
    {
        return Arrays.copyOf(genes, genes.length);
    }

    @Override
    public int size()
    {
        return genes.length;
    }

    @Override
    public boolean equals(Object obj)
    {
        return false;
    }

    @Override
    public abstract Chromosome<T> clone();
}
