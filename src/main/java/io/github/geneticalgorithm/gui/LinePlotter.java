package io.github.geneticalgorithm.gui;

import io.github.geneticalgorithm.Epoch;
import io.github.geneticalgorithm.callback.Callback;
import io.github.lightdataframe.lightweightdataframe.Dataframe;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.None;

import java.awt.*;
import java.util.*;

public abstract class LinePlotter<T> implements Callback<T>
{
    Dataframe df;
    XYChart chart;
    private XChartPanel<XYChart> panel;

    private final Object drawLock = new Object();

    protected LinePlotter(String chartTitle)
    {
        df = new Dataframe();
        chart = new XYChart(600, 400);
        panel = new XChartPanel<>(chart);
        chart.setTitle(chartTitle);
    }

    @Override
    public void onTrainingStart()
    {
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
        Map<String, Double> data = getPlotData(epoch);
        synchronized (drawLock)
        {
            df.append(true, data);
        }

        FrameManager.getInstance().runSynchronously(() -> {
            for(XYSeries series : new ArrayList<>(chart.getSeriesCollection()))
                chart.removeSeries(series.getName());

            synchronized (drawLock)
            {
                for(String column : df.getColumns())
                {
                    chart.addSeries(column, df.getColumn(column))
                            .setMarker(new None())
                            .setLineStyle(new BasicStroke());
                }
            }
            panel.repaint();
        });
    }

    protected abstract Map<String, Double> getPlotData(Epoch<T> epoch);
}
