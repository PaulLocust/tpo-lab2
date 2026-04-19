package lab.pavel.function.trig;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DerivedTrigModulesTest {
    private static final double EPS = 1e-6;

    private final CosSeries cos = new CosSeries(1e-12, 100_000);
    private final SinFromCos sin = new SinFromCos(cos);
    private final TanModule tan = new TanModule(sin, cos);
    private final CotModule cot = new CotModule(sin, cos);
    private final SecModule sec = new SecModule(cos);
    private final CscModule csc = new CscModule(sin);

    @ParameterizedTest
    @CsvSource({
            "-2.0, 2.1850398633, 0.4576575544, -2.4029979617, -1.0997501703",
            "-0.7, -0.8422883805, -1.1872418321, 1.3074592597, -1.5522703270",
            "-0.5, -0.5463024898, -1.8304877217, 1.1394939273, -2.0858296429"
    })
    void shouldCalculateDerivedTrigModules(double x, double tanExpected, double cotExpected,
                                           double secExpected, double cscExpected) {
        assertEquals(tanExpected, tan.calculate(x), EPS);
        assertEquals(cotExpected, cot.calculate(x), EPS);
        assertEquals(secExpected, sec.calculate(x), EPS);
        assertEquals(cscExpected, csc.calculate(x), EPS);
    }

    @ParameterizedTest
    @CsvSource({
            "1.5707963268",
            "-1.5707963268"
    })
    void shouldReturnNaNForTanAndSecAtCosZeros(double x) {
        assertTrue(Double.isNaN(tan.calculate(x)));
        assertTrue(Double.isNaN(sec.calculate(x)));
    }

    @ParameterizedTest
    @CsvSource({
            "0.0",
            "3.1415926536"
    })
    void shouldReturnNaNForCotAndCscAtSinZeros(double x) {
        assertTrue(Double.isNaN(cot.calculate(x)));
        assertTrue(Double.isNaN(csc.calculate(x)));
    }
}