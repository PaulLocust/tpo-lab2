package lab.pavel.function.trig;

import lab.pavel.function.MathModule;

public class CscModule implements MathModule {
    private static final double EPS = 1e-10;

    private final MathModule sinModule;

    public CscModule(MathModule sinModule) {
        this.sinModule = sinModule;
    }

    @Override
    public double calculate(double x) {
        double sin = sinModule.calculate(x);
        if (Math.abs(sin) < EPS) {
            return Double.NaN;
        }
        return 1.0 / sin;
    }
}