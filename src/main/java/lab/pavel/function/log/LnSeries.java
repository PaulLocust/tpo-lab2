package lab.pavel.function.log;

import lab.pavel.function.MathModule;

public class LnSeries implements MathModule {
    private final double epsilon;
    private final int maxIterations;

    public LnSeries(double epsilon, int maxIterations) {
        this.epsilon = epsilon;
        this.maxIterations = maxIterations;
    }

    @Override
    public double calculate(double x) {
        if (x <= 0.0) {
            return Double.NaN;
        }

        double z = (x - 1.0) / (x + 1.0);
        double zSquared = z * z;
        double term = z;
        double sum = 0.0;

        for (int n = 0; n < maxIterations; n++) {
            double addend = term / (2.0 * n + 1.0);
            sum += addend;
            if (Math.abs(addend) < epsilon) {
                break;
            }
            term *= zSquared;
        }

        return 2.0 * sum;
    }
}