package examples.sensors;

import io.github.geneticalgorithm.chromosome.Chromosome;
import examples.sensors.model.Environment;
import examples.sensors.model.Location;
import examples.sensors.model.Sensor;
import examples.sensors.model.SensorChromosome;
import examples.sensors.model.cost.CostModel;


public class EnvModel
{
    private Environment environment;
    private CostModel costModel;

    public EnvModel(Environment environment, CostModel costModel)
    {
        this.environment = environment;
        this.costModel = costModel;
    }

    public Environment getEnvironment()
    {
        return environment;
    }

    public Chromosome<SensorChromosome.Gene> getSensorsChromosome()
    {
        SensorChromosome.Gene[] genes = environment.getSensors().stream()
                .map(sensor -> new SensorChromosome.Gene(
                        environment.getMinX(),
                        environment.getMaxX(),
                        environment.getMinY(),
                        environment.getMaxY(),
                        sensor.getPositionX(),
                        sensor.getPositionY(),
                        sensor.cost,
                        sensor.precision,
                        getAssignments(sensor),
                        10
                )).toArray(SensorChromosome.Gene[]::new);

        return new SensorChromosome(genes, this);
    }

    public void setSensorsFromChromosome(Chromosome<SensorChromosome.Gene> chromosome)
    {
        environment.clearAssignments();
        environment.clearSensors();

        for(int i = 0; i < chromosome.size(); i++)
            addGeneToEnvironment(chromosome.getGene(i));

    }

    public synchronized double computeFitness(Chromosome<SensorChromosome.Gene> chromosome)
    {
        setSensorsFromChromosome(chromosome);
        return - costModel.getEnvironmentCost(environment);
    }


    private void addGeneToEnvironment(SensorChromosome.Gene gene)
    {
        if(gene.assignments.length != environment.getPOIs().size())
            throw new IllegalArgumentException("Number of assignments do not match");
        Sensor sensor = new Sensor(gene.positionX, gene.positionY, gene.cost, gene.precision);
        environment.addSensor(sensor);
        for(int i = 0; i < gene.assignments.length; i++)
            if(gene.assignments[i])
                environment.addAssignment(sensor, environment.getPOIs().get(i));
    }

    private boolean[] getAssignments(Sensor sensor)
    {
        boolean[] assignments = new boolean[environment.getPOIs().size()];

        for(int i = 0; i < environment.getPOIs().size(); i++)
        {
            Location loc = environment.getPOIs().get(i);
            if(environment.isAssignedTo(sensor, loc))
                assignments[i] = true;
        }
        return assignments;
    }
}
