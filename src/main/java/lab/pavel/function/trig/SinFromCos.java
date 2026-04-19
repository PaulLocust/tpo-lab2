package lab.pavel.function.trig;

import lab.pavel.function.MathModule;

public class SinFromCos implements MathModule {
    private final MathModule cosModule;

    public SinFromCos(MathModule cosModule) {
        this.cosModule = cosModule;
    }

    @Override
    public double calculate(double x) {
        return cosModule.calculate(Math.PI / 2.0 - x);
    }
}