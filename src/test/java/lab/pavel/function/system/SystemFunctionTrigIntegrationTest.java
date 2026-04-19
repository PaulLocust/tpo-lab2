package lab.pavel.function.system;

import lab.pavel.function.MathModule;
import lab.pavel.function.trig.CosSeries;
import lab.pavel.function.trig.CotModule;
import lab.pavel.function.trig.CscModule;
import lab.pavel.function.trig.SecModule;
import lab.pavel.function.trig.SinFromCos;
import lab.pavel.function.trig.TanModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class SystemFunctionTrigIntegrationTest {
    private static final double EPS = 1e-5;

    private final MathModule cos = new CosSeries(1e-12, 100_000);
    private final MathModule sin = new SinFromCos(cos);
    private final MathModule tan = new TanModule(sin, cos);
    private final MathModule cot = new CotModule(sin, cos);
    private final MathModule sec = new SecModule(cos);
    private final MathModule csc = new CscModule(sin);

    private final MathModule ln = mock(MathModule.class);
    private final MathModule log2 = mock(MathModule.class);
    private final MathModule log3 = mock(MathModule.class);
    private final MathModule log5 = mock(MathModule.class);
    private final MathModule log10 = mock(MathModule.class);

    private final SystemFunction system = new SystemFunction(cos, sin, tan, cot, sec, csc, ln, log2, log3, log5, log10);

    @ParameterizedTest
    @CsvSource({
            "-2.0, 0.5422491517",
            "-1.5, 0.5012209166",
            "-0.7, -0.0000098006",
            "-0.5, -116.7025883623"
    })
    void shouldCalculateNegativeBranchWithRealTrigModules(double x, double expected) {
        assertEquals(expected, system.calculate(x), EPS);
    }

    @Test
    void shouldReturnNaNAtCutoutPointNearSinZero() {
        assertTrue(Double.isNaN(system.calculate(0.0)));
        assertTrue(Double.isNaN(system.calculate(-Math.PI)));
    }

    @Test
    void shouldProduceLargeMagnitudeNearAsymptoteAtZero() {
        double value = system.calculate(-0.1);
        assertTrue(Math.abs(value) > 1_000_000.0);
    }
}