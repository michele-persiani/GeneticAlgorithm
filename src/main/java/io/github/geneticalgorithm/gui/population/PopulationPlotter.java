package io.github.geneticalgorithm.gui.population;

import io.github.geneticalgorithm.Epoch;
import io.github.geneticalgorithm.callback.Callback;
import io.github.geneticalgorithm.chromosome.Chromosome;
import io.github.geneticalgorithm.gui.FrameManager;
import org.hipparchus.stat.projection.PCA;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.Circle;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PopulationPlotter<T> implements Callback<T>
{

    private final XYChart chart;
    private final XChartPanel<XYChart> panel;
    private final FeatureTransform<T> transform;
    private PCA pca;
    private BrightnessStrategy brightnessStrategy = new MinMaxBrightnessStrategy();
    private PopulationStrategy<T> populationStrategy = new EpochPopulationStrategy<>();

    private int recomputePCAEveryN = 200;

    public PopulationPlotter(FeatureTransform<T> transform)
    {
        this.transform = transform;
        chart = new XYChart(600, 400);
        panel = new XChartPanel<>(chart);
        chart.setTitle("Population");

    }

    public void setBrightnessStrategy(BrightnessStrategy brightnessStrategy)
    {
        this.brightnessStrategy = brightnessStrategy;
    }

    public void setPopulationStrategy(PopulationStrategy<T> populationStrategy)
    {
        this.populationStrategy = populationStrategy;
    }

    public void setRecomputePCAEveryN(int n)
    {
        this.recomputePCAEveryN = n;
    }

    @Override
    public void onTrainingStart()
    {
        pca = null;
        FrameManager.getInstance().addModule(panel);
        FrameManager.getInstance().show();
    }

    @Override
    public void onEpochStart(int epochNum)
    {
    }

    @Override
    public void onTrainingFinish(Epoch<T> lastEpoch)
    {
    }

    @Override
    public void onEpochFinish(Epoch<T> epoch)
    {
        Map<Chromosome<T>, Double> population = populationStrategy.getPlotPopulation(epoch);
        population = new LinkedHashMap<>(population);

        double[][] populationData = getPopulationData(population);

        if(pca == null || epoch.getEpochNum() % recomputePCAEveryN == 0)
        {
            pca = new PCA(2);
            pca.fit(populationData);
        }

        double[][] transformedXY = pca.transform(populationData);

        double[] fitnesses = population.values().stream().mapToDouble(aDouble -> aDouble).toArray();


        for(XYSeries series : new ArrayList<>(chart.getSeriesCollection()))
            chart.removeSeries(series.getName());

        for(int i = 0; i < transformedXY.length; i++)
        {
            chart.addSeries(String.format("%s", i), new double[]{transformedXY[i][0]}, new double[]{transformedXY[i][1]})
                    .setMarkerColor(Color.getHSBColor(0.5f, 1,  (float) brightnessStrategy.getBrightness(fitnesses, i)))
                    .setMarker(new Circle())
                    .setShowInLegend(false);
        }
        FrameManager.getInstance().repaint();
    }



    private double[][] getPopulationData(Map<Chromosome<T>, Double> population)
    {
        List<double[]> features = population
                .keySet()
                .stream()
                .map(transform::transform)
                .toList();

        double[][] populationData = new double[features.size()][features.get(0).length];
        for(int i = 0; i < features.size(); i++)
            System.arraycopy(features.get(i), 0, populationData[i], 0, features.get(i).length);
        return populationData;
    }

}
