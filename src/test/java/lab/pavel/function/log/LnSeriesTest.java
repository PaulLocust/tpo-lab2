package lab.pavel.function.log;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LnSeriesTest {
    private static final double EPS = 1e-6;

    private final LnSeries lnSeries = new LnSeries(1e-12, 500_000);

    @ParameterizedTest
    @CsvSource({
            "0.1, -2.3025850930",
            "0.2, -1.6094379124",
            "0.5, -0.6931471806",
            "0.7, -0.3566749439",
            "1.0, 0.0",
            "2.0, 0.6931471806",
            "3.0, 1.0986122887",
            "10.0, 2.3025850930"
    })
    void shouldCalculateLn(double x, double expected) {
        assertEquals(expected, lnSeries.calculate(x), EPS);
    }

    @ParameterizedTest
    @CsvSource({"0.0", "-0.5", "-2.0"})
    void shouldReturnNaNOutsideDomain(double x) {
        assertTrue(Double.isNaN(lnSeries.calculate(x)));
    }

    @Test
    void shouldReturnZeroWhenMaxIterationsIsZero() {
        LnSeries noIterations = new LnSeries(1e-12, 0);
        assertEquals(0.0, noIterations.calculate(2.0), 1e-12);
    }
}