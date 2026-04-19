package lab.pavel.function.trig;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CosSeriesTest {
    private static final double EPS = 1e-7;

    private final CosSeries cosSeries = new CosSeries(1e-12, 100_000);

    @ParameterizedTest
    @CsvSource({
            "0.0, 1.0",
            "1.5707963268, 0.0",
            "3.1415926536, -1.0",
            "6.2831853072, 1.0",
            "-1.0, 0.5403023059",
            "-2.0, -0.4161468365"
    })
    void shouldCalculateCosWithSeries(double x, double expected) {
        assertEquals(expected, cosSeries.calculate(x), EPS);
    }

    @ParameterizedTest
    @CsvSource({
            "-2.0",
            "-1.5",
            "-0.7",
            "0.3",
            "1.2"
    })
    void shouldBePeriodic(double x) {
        double period = 2.0 * Math.PI;
        assertEquals(cosSeries.calculate(x), cosSeries.calculate(x + period), EPS);
    }

    @Test
    void shouldHandleNegativeAngleLessThanPiAfterNormalization() {
        assertEquals(Math.cos(-4.0), cosSeries.calculate(-4.0), EPS);
    }

    @Test
    void shouldReturnInitialTermWhenNoIterationsAllowed() {
        CosSeries noIterations = new CosSeries(0.0, 0);
        assertEquals(1.0, noIterations.calculate(0.3), 1e-12);
    }
}