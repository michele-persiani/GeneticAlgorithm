package examples.sensors.gui;

import io.github.geneticalgorithm.gui.BestChromosomePlotter;
import io.github.geneticalgorithm.chromosome.Chromosome;
import examples.sensors.EnvModel;
import examples.sensors.model.Location;
import examples.sensors.model.SensorChromosome;

import java.awt.*;

public class PlotBestSensorChromosomeCallback extends BestChromosomePlotter<SensorChromosome.Gene>
{
    private final EnvModel envModel;


    public PlotBestSensorChromosomeCallback(EnvModel envModel)
    {
        super();
        this.envModel = envModel;
    }

    @Override
    protected Canvas createCanvas()
    {
        return new Canvas(
                envModel.getEnvironment().getMinX(),
                envModel.getEnvironment().getMaxX(),
                envModel.getEnvironment().getMinY(),
                envModel.getEnvironment().getMaxY()
        );
    }

    @Override
    protected void drawChromosome(Chromosome<SensorChromosome.Gene> best, Canvas canvas)
    {
        envModel.setSensorsFromChromosome(best);

        canvas.clear();
        envModel.getEnvironment()
                .getLakes()
                .forEach(l -> canvas.drawCircle(l.x, l.y, l.radius, Color.blue));
        envModel.getEnvironment()
                .getCities()
                .forEach(l -> canvas.drawCircle(l.x, l.y, l.radius, Color.lightGray));

        envModel.getEnvironment()
                .getPOIs()
                .forEach(loc -> canvas.drawPoint(loc.getPositionX(), loc.getPositionY(), Color.black));
        envModel.getEnvironment()
                .getSensors()
                .forEach(s -> {
                    canvas.drawPoint(s.getPositionX(), s.getPositionY(), Color.red);
                    for(Location poi : envModel.getEnvironment().getAssignments(s))
                    {
                        canvas.drawLine(s.getPositionX(), s.getPositionY(), poi.getPositionX(), poi.getPositionY(), Color.red);
                    }
                });
    }
}
