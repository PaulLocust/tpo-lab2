package lab.pavel.function.trig;

import lab.pavel.function.MathModule;

public class CotModule implements MathModule {
    private static final double EPS = 1e-10;

    private final MathModule sinModule;
    private final MathModule cosModule;

    public CotModule(MathModule sinModule, MathModule cosModule) {
        this.sinModule = sinModule;
        this.cosModule = cosModule;
    }

    @Override
    public double calculate(double x) {
        double sin = sinModule.calculate(x);
        double cos = cosModule.calculate(x);
        if (Math.abs(sin) < EPS) {
            return Double.NaN;
        }
        return cos / sin;
    }
}