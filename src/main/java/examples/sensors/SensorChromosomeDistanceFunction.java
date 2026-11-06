package examples.sensors;

import examples.sensors.model.SensorChromosome;
import io.github.geneticalgorithm.chromosome.Chromosome;
import io.github.geneticalgorithm.distance.DistanceFunction;

public class SensorChromosomeDistanceFunction implements DistanceFunction<SensorChromosome.Gene>
{
    @Override
    public double computeDistance(Chromosome<SensorChromosome.Gene> first, Chromosome<SensorChromosome.Gene> second)
    {
        double totalDistance = 0;
        for(int i = 0; i < first.size(); i++)
        {
            SensorChromosome.Gene g0 = first.getGene(i);
            SensorChromosome.Gene closest = g0;
            double minXyDistance = Double.MAX_VALUE;
            for(int j = 0; j < first.size(); j++)
            {
                SensorChromosome.Gene g1 = second.getGene(j);
                double xyDistance = Math.pow(g0.positionX - g1.positionX, 2) + Math.pow(g0.positionY - g1.positionY, 2);
                if(xyDistance < minXyDistance)
                {
                    minXyDistance = xyDistance;
                    closest = g1;
                }
            }
            totalDistance += computeDistance(g0, closest);
        }

        return totalDistance;
    }

    private double computeDistance(SensorChromosome.Gene g0, SensorChromosome.Gene g1)
    {
        return Math.pow(g0.positionX - g1.positionX, 2) + Math.pow(g0.positionY - g1.positionY, 2);
    }
}
