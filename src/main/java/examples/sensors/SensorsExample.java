package examples.sensors;

import examples.sensors.model.*;
import io.github.geneticalgorithm.GeneticAlgorithm;
import io.github.geneticalgorithm.callback.FitnessStatisticsCallback;
import io.github.geneticalgorithm.chromosome.Chromosome;
import io.github.geneticalgorithm.endcondition.EndConditionFactory;
import examples.sensors.gui.PlotBestSensorChromosomeCallback;
import examples.sensors.model.cost.CostModel;
import examples.sensors.model.cost.DefaultCostModel;
import io.github.geneticalgorithm.gui.FitnessPlotter;
import io.github.geneticalgorithm.gui.population.NoOutliersMinMaxBrightnessStrategy;
import io.github.geneticalgorithm.gui.population.PopulationPlotter;
import io.github.geneticalgorithm.replacement.GeneralizedReplacementStrategy;
import io.github.geneticalgorithm.selection.NichingSelectionStrategy;
import io.github.geneticalgorithm.selection.TournamentSelection;

import java.util.Random;




public class SensorsExample
{
    public static void main(String[] args)
    {
        Environment env = createEnvironment();

        CostModel costModel = new DefaultCostModel(
                1e16,
                3,
                3e-1,
                100,
                10000
        );

        EnvModel model = new EnvModel(env, costModel);

        GeneticAlgorithm<SensorChromosome.Gene> ga = new GeneticAlgorithm<>();

        EndConditionFactory endConditionFactory = new EndConditionFactory();

        ga.setChromosomePrototype(model.getSensorsChromosome());
        ga.setMutationProbability(0.5);
        ga.setPopulationSize(300);
        ga.setKeepBestN(10);

        GeneralizedReplacementStrategy<SensorChromosome.Gene> crowding = new GeneralizedReplacementStrategy<>(new SensorChromosomeDistanceFunction());
        crowding.setScalingFactor(0.1);
        //ga.setReplacementStrategy(crowding);

        ga.setSelectionStrategy(
                new NichingSelectionStrategy<>(
                        5,
                        new SensorChromosomeDistanceFunction(),
                        () -> new TournamentSelection<>(10)
                )
        );

        ga.setEndCondition(endConditionFactory.createEpochNumReached(5000));

        ga.addCallback(new FitnessStatisticsCallback<>());
        ga.addCallback(new PlotBestSensorChromosomeCallback(model));

        FitnessPlotter<SensorChromosome.Gene> fitnessPlotter = new FitnessPlotter<>();
        fitnessPlotter.setScaleFactor(-1);
        fitnessPlotter.setUseLog10(true);
        ga.addCallback(fitnessPlotter);

        PopulationPlotter<SensorChromosome.Gene> populationPlotter = new PopulationPlotter<>(SensorsExample::chromosomeToArray);
        populationPlotter.setBrightnessStrategy(new NoOutliersMinMaxBrightnessStrategy(1));

        ga.addCallback(populationPlotter);

        ga.run();
    }

    private static double[] chromosomeToArray(Chromosome<SensorChromosome.Gene> ch)
    {
        int geneSize = 4 + ch.getGene(0).assignments.length;
        double[] values = new double[ch.size() * geneSize];

        for(int i = 0; i < ch.size(); i++)
        {
            SensorChromosome.Gene g = ch.getGene(i);
            int j = i * geneSize;
            values[j] = g.positionX;
            values[j + 1] = g.positionY;
            values[j + 2] = g.precision;
            values[j + 3] = g.cost;
            for(int k = 0; k < g.assignments.length; k++)
                values[j + 4 + k] = g.assignments[k] ? 1 : 0;
        }
        return values;

    }

    private static Environment createEnvironment()
    {
        int numLakes = 5;
        int numCities = 5;
        int numSensors = 20;
        int numPOIs = 20;
        double minX = 0;
        double maxX = 100;
        double minY = 0;
        double maxY = 100;

        Environment env = new Environment(minX, maxX, minY, maxY);
        for(int i = 0; i < numLakes; i++)
        {
            Location l = createRandomLocation(minX, maxX, minY, maxY);
            env.addLake(new Circle(l.getPositionX(), l.getPositionY(), random.nextDouble(50, 500)));
        }
        for(int i = 0; i < numCities; i++)
        {
            Location l = createRandomLocation(minX, maxX, minY, maxY);
            env.addCity(new Circle(l.getPositionX(), l.getPositionY(), random.nextDouble(50, 500)));
        }

        for(int i = 0; i < numSensors; i++)
        {
            Sensor s = createSensor(minX, maxX, minY, maxY);
            env.addSensor(s);
        }
        for(int i = 0; i < numPOIs; i++)
        {
            Location l = createRandomLocation(minX, maxX, minY, maxY);
            env.addPOI(l);
        }
        return env;
    }

    private static final Random random = new Random();

    private static Sensor createSensor(double minX, double maxX, double minY, double maxY)
    {
        SensorType[] sensorTypes = SensorType.values();
        SensorType type = sensorTypes[random.nextInt(sensorTypes.length)];
        return new Sensor(
                random.nextDouble(minX, maxX),
                random.nextDouble(minY, maxY),
                type.getCost(),
                type.getPrecision()
        );
    }

    private static Location createRandomLocation(double minX, double maxX, double minY, double maxY)
    {
        return new Location(
                random.nextDouble(minX, maxX),
                random.nextDouble(minY, maxY)
        );
    }

    public enum SensorType
    {
        EFFICIENT(1, 0);
        //BAD(30, 30),
        //EXPENSIVE(30, 10),
        //CHEAP(10, 30);

        private double cost;
        private double precision;

        SensorType(double cost, double precision)
        {
            this.cost = cost;
            this.precision = precision;
        }

        public double getCost()
        {
            return cost;
        }

        public double getPrecision()
        {
            return precision;
        }
    }
}
