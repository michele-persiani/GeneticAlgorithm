package io.github.geneticalgorithm.chromosome;



public interface Chromosome<T> extends Cloneable
{
    T getGene(int position);

    void setGene(int position, T gene);

    int size();

    void mutate(int position);

    Chromosome<T> clone();

    void randomize(int position);

    double computeFitness();

    default void randomize()
    {
        for(int i = 0; i < size(); i++)
            randomize(i);
    }

    /**
     * Check for equality with another object. Must return false
     * @param o
     * @return false
     */
    boolean equals(Object o);
}
