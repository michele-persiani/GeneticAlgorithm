package io.github.geneticalgorithm.gui;

import io.github.geneticalgorithm.Epoch;
import io.github.geneticalgorithm.callback.Callback;
import io.github.geneticalgorithm.chromosome.Chromosome;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public abstract class BestChromosomePlotter<T> implements Callback<T>
{
    private Canvas canvas;

    public BestChromosomePlotter()
    {
    }

    @Override
    public void onTrainingStart()
    {
        this.canvas = createCanvas();
        JFrame frame = new JFrame("Best Chromosome");
        frame.add(canvas);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void onEpochStart(int epochNum)
    {

    }

    @Override
    public void onEpochFinish(Epoch<T> epoch)
    {
        Chromosome<T> best = epoch.getBestGene();
        canvas.clear();
        drawChromosome(best, canvas);
        SwingUtilities.invokeLater(canvas::repaint);
    }

    @Override
    public void onTrainingFinish(Epoch<T> lastEpoch)
    {

    }

    protected abstract Canvas createCanvas();

    protected abstract void drawChromosome(Chromosome<T> ch, Canvas canvas);

    public static class Canvas extends JPanel
    {
        private final double minX, maxX, minY, maxY;

        private final Collection<Consumer<Graphics2D>> painters = new ConcurrentLinkedQueue<>();

        public Canvas(double minX, double maxX, double minY, double maxY)
        {
            this.minX = minX;
            this.maxX = maxX;
            this.minY = minY;
            this.maxY = maxY;
        }

        public Canvas clear()
        {
            painters.clear();
            return this;
        }

        public Canvas drawLine(double fromX, double fromY, double toX, double toY, Color color)
        {
            painters.add(g -> {
                double fx = (fromX - minX) / (maxX - minX);
                double fy = (fromY - minY) / (maxY - minY);
                double tx = (toX - minX) / (maxX - minX);
                double ty = (toY - minY) / (maxY - minY);
                g.setColor(color);
                g.drawLine((int) (fx * getWidth()),
                        (int) (fy * getHeight()),
                        (int) (tx * getWidth()),
                        (int) (ty * getHeight())
                );
            });
            return this;
        }

        public Canvas drawPoint(double x, double y, Color color)
        {
            painters.add(g -> {
                double px = (x - minX) / (maxX - minX);
                double py = (y - minY) / (maxY - minY);
                g.setColor(color);
                g.fillOval((int) (px * getWidth()) - 4, (int) (py * getHeight()) - 4, 8, 8);
            });
            return this;
        }

        public Canvas drawCircle(double x, double y, double radius, Color color)
        {
            painters.add(g -> {
                double px = (x - minX) / (maxX - minX);
                double py = (y - minY) / (maxY - minY);
                g.setColor(color);
                g.fillOval(
                        (int) (px * getWidth() - radius / 2),
                        (int) (py * getHeight() - radius / 2),
                        (int) (2 * radius),
                        (int) (2 * radius)
                );
            });
            return this;
        }


        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            painters.forEach(p -> p.accept(g2d));
        }
    }

}
