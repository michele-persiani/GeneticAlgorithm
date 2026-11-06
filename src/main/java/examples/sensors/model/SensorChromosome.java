package examples.sensors.model;

import examples.sensors.EnvModel;
import io.github.geneticalgorithm.chromosome.ArrayChromosome;
import io.github.geneticalgorithm.chromosome.Chromosome;

import java.util.Arrays;
import java.util.Random;

public class SensorChromosome extends ArrayChromosome<SensorChromosome.Gene>
{
    private static final Random random = new Random();

    private EnvModel model;

    public SensorChromosome(Gene[] genes, EnvModel model)
    {
        super(genes);
        this.model = model;
    }

    @Override
    public void mutate(int position)
    {
        getGene(position).mutate();
    }

    @Override
    public Chromosome<Gene> clone()
    {
        return new SensorChromosome(
                Arrays.stream(genes).map(Gene::clone).toArray(Gene[]::new),
                model
        );
    }

    @Override
    public void randomize(int position)
    {
        getGene(position).randomize();
    }

    @Override
    public double computeFitness()
    {
        return model.computeFitness(this);
    }


    public static class Gene
    {
        private final double minPositionX;
        private final double minPositionY;
        private final double maxPositionX;
        private final double maxPositionY;
        public double positionX;
        public double positionY;
        public double cost;
        public double precision;
        public final boolean[] assignments;
        private final double mutationRadius;

        public Gene(
                double minPositionX,
                double maxPositionX,
                double minPositionY,
                double maxPositionY,
                double positionX,
                double positionY,
                double cost,
                double precision,
                boolean[] assignments,
                double mutationRadius
        )
        {
            this.minPositionX = minPositionX;
            this.maxPositionX = maxPositionX;
            this.minPositionY = minPositionY;

            this.maxPositionY = maxPositionY;
            this.positionX = positionX;
            this.positionY = positionY;
            this.cost = cost;
            this.precision = precision;
            this.assignments = assignments;
            this.mutationRadius = mutationRadius;
        }

        public void mutate()
        {
            boolean mutatePosition = random.nextDouble() < 0.5;
            if(mutatePosition)
            {
                positionX += random.nextDouble(- mutationRadius, mutationRadius);
                positionY += random.nextDouble(- mutationRadius, mutationRadius);
                positionX = Math.max(Math.min(positionX, maxPositionX), minPositionX);
                positionY = Math.max(Math.min(positionY, maxPositionY), minPositionY);
            }
            else
            {
                int assignment = random.nextInt(assignments.length);
                assignments[assignment] = !assignments[assignment];
            }
        }

        public void randomize()
        {
            positionX = random.nextDouble(minPositionX, maxPositionX);
            positionY = random.nextDouble(minPositionY, maxPositionY);
            for(int i = 0; i < assignments.length; i++)
                assignments[i] = random.nextBoolean();
        }

        public Gene clone()
        {
            return new Gene(
                    minPositionX,
                    maxPositionX,
                    minPositionY,
                    maxPositionY,
                    positionX,
                    positionY,
                    cost,
                    precision,
                    Arrays.copyOf(assignments, assignments.length),
                    mutationRadius
            );
        }
    }
}
