package lab.pavel.function.system;

import lab.pavel.function.MathModule;

public class SystemFunction implements MathModule {
    private static final double EPS = 1e-10;

    private final MathModule cosModule;
    private final MathModule sinModule;
    private final MathModule tanModule;
    private final MathModule cotModule;
    private final MathModule secModule;
    private final MathModule cscModule;
    private final MathModule lnModule;
    private final MathModule log2Module;
    private final MathModule log3Module;
    private final MathModule log5Module;
    private final MathModule log10Module;

    public SystemFunction(
            MathModule cosModule,
            MathModule sinModule,
            MathModule tanModule,
            MathModule cotModule,
            MathModule secModule,
            MathModule cscModule,
            MathModule lnModule,
            MathModule log2Module,
            MathModule log3Module,
            MathModule log5Module,
            MathModule log10Module
    ) {
        this.cosModule = cosModule;
        this.sinModule = sinModule;
        this.tanModule = tanModule;
        this.cotModule = cotModule;
        this.secModule = secModule;
        this.cscModule = cscModule;
        this.lnModule = lnModule;
        this.log2Module = log2Module;
        this.log3Module = log3Module;
        this.log5Module = log5Module;
        this.log10Module = log10Module;
    }

    @Override
    public double calculate(double x) {
        if (x <= 0.0) {
            return calculateTrigonometricBranch(x);
        }
        return calculateLogarithmicBranch(x);
    }

    private double calculateTrigonometricBranch(double x) {
        double cos = cosModule.calculate(x);
        double sin = sinModule.calculate(x);
        double tan = tanModule.calculate(x);
        double cot = cotModule.calculate(x);
        double sec = secModule.calculate(x);
        double csc = cscModule.calculate(x);

        double part1 = divide(cos, sin);
        part1 = part1 * sin;
        part1 = cube(part1);
        part1 = square(part1);
        part1 = part1 - cot;
        part1 = part1 - sec;
        part1 = part1 * cos;
        part1 = square(part1);
        part1 = divide(part1, csc + cube(sin));
        part1 = part1 * divide(cot, cot);
        part1 = part1 - sin;

        double extraTerm = divide(tan, cube(square((tan - csc) + cos))) * (sec - cot);
        part1 += extraTerm;

        double part2 = divide(
                cos * cube(csc - sin),
                cube(divide(cube(tan) * ((cot + sin) * cos), cos))
        );
        part2 = part2 * (cot * (square(divide(sin * cube(cos), sin)) - sin));

        return part1 - part2;
    }

    private double calculateLogarithmicBranch(double x) {
        double ln = lnModule.calculate(x);
        double log2 = log2Module.calculate(x);
        double log3 = log3Module.calculate(x);
        double log5 = log5Module.calculate(x);
        double log10 = log10Module.calculate(x);

        double left = divide(log3 + ln, log2) - (ln * log5);
        return square(left) - (log2 * log10);
    }

    private double divide(double a, double b) {
        if (Double.isNaN(a) || Double.isNaN(b) || Math.abs(b) < EPS) {
            return Double.NaN;
        }
        return a / b;
    }

    private double square(double value) {
        return value * value;
    }

    private double cube(double value) {
        return value * value * value;
    }
}