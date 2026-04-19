package lab.pavel.function.trig;

import lab.pavel.function.MathModule;

public class SecModule implements MathModule {
    private static final double EPS = 1e-10;

    private final MathModule cosModule;

    public SecModule(MathModule cosModule) {
        this.cosModule = cosModule;
    }

    @Override
    public double calculate(double x) {
        double cos = cosModule.calculate(x);
        if (Math.abs(cos) < EPS) {
            return Double.NaN;
        }
        return 1.0 / cos;
    }
}