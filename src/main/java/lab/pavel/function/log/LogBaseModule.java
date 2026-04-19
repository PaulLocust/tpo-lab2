package lab.pavel.function.log;

import lab.pavel.function.MathModule;

public class LogBaseModule implements MathModule {
    private static final double EPS = 1e-10;

    private final double base;
    private final MathModule lnModule;

    public LogBaseModule(double base, MathModule lnModule) {
        if (base <= 0.0 || Math.abs(base - 1.0) < EPS) {
            throw new IllegalArgumentException("Log base must be positive and not equal to 1");
        }
        this.base = base;
        this.lnModule = lnModule;
    }

    @Override
    public double calculate(double x) {
        double lnX = lnModule.calculate(x);
        if (Double.isNaN(lnX)) {
            return Double.NaN;
        }
        double lnBase = lnModule.calculate(base);
        if (Double.isNaN(lnBase) || Math.abs(lnBase) < EPS) {
            return Double.NaN;
        }
        return lnX / lnBase;
    }
}