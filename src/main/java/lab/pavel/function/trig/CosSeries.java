package lab.pavel.function.trig;

import lab.pavel.function.MathModule;

public class CosSeries implements MathModule {
    private static final double TWO_PI = 2.0 * Math.PI;

    private final double epsilon;
    private final int maxIterations;

    public CosSeries(double epsilon, int maxIterations) {
        this.epsilon = epsilon;
        this.maxIterations = maxIterations;
    }

    @Override
    public double calculate(double x) {
        double normalizedX = normalizeAngle(x);
        double sum = 1.0;
        double term = 1.0;

        for (int n = 1; n <= maxIterations; n++) {
            double multiplier = -normalizedX * normalizedX / ((2.0 * n - 1.0) * (2.0 * n));
            term *= multiplier;
            sum += term;
            if (Math.abs(term) < epsilon) {
                break;
            }
        }

        return sum;
    }

    private double normalizeAngle(double x) {
        double result = x % TWO_PI;
        if (result > Math.PI) {
            result -= TWO_PI;
        }
        if (result < -Math.PI) {
            result += TWO_PI;
        }
        return result;
    }
}
