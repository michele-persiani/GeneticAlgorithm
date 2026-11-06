package examples.xyfunctions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DistanceBasedFitness
{
    private static class DistanceCalculator
    {
        private double minDistance;
        private double cx;
        private double cy;
        private double baseFitness;

        private DistanceCalculator(double cx, double cy, double baseFitness, double minDistance)
        {
            this.minDistance = minDistance;
            this.cx = cx;
            this.cy = cy;
            this.baseFitness = baseFitness;
        }

        public double computeFitness(double x, double y)
        {
            double d = Math.sqrt(Math.pow(x - cx, 2) + Math.pow(y - cy, 2));
            if(d > minDistance)
                return 0;
            return baseFitness -d;
        }
    }

    private final List<DistanceCalculator> distanceCalculators = new ArrayList<>();

    private double minDistance = Double.MAX_VALUE;
    private double baseFitness = 0;

    public DistanceBasedFitness setBaseFitness(double baseFitness)
    {
        this.baseFitness = baseFitness;
        return this;
    }

    public DistanceBasedFitness setMinDistance(double minDistance)
    {
        this.minDistance = minDistance;
        return this;
    }

    public DistanceBasedFitness addCenter(double cx, double cy)
    {
        distanceCalculators.add(new DistanceCalculator(cx, cy, baseFitness, minDistance));
        return this;
    }

    public double computeFitness(double x, double y)
    {
        return distanceCalculators.stream()
                .map(d -> d.computeFitness(x, y))
                .max(Comparator.naturalOrder())
                .orElse(Double.MIN_VALUE);
    }
}
