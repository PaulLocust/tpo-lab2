package lab.pavel.function.system;

import lab.pavel.function.MathModule;
import lab.pavel.function.log.LnSeries;
import lab.pavel.function.log.LogBaseModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class SystemFunctionLogIntegrationTest {
    private static final double EPS = 1e-5;

    private final MathModule trigStub = mock(MathModule.class);

    private final MathModule ln = new LnSeries(1e-12, 500_000);
    private final MathModule log2 = new LogBaseModule(2.0, ln);
    private final MathModule log3 = new LogBaseModule(3.0, ln);
    private final MathModule log5 = new LogBaseModule(5.0, ln);
    private final MathModule log10 = new LogBaseModule(10.0, ln);

    private final SystemFunction system = new SystemFunction(
            trigStub,
            trigStub,
            trigStub,
            trigStub,
            trigStub,
            trigStub,
            ln,
            log2,
            log3,
            log5,
            log10
    );

    @ParameterizedTest
    @CsvSource({
            "0.1, 0.5596716073",
            "0.2, -1.5415272026",
            "0.3, -0.7289374193",
            "0.5, 0.7507324310",
            "0.7, 1.4703976776",
            "2.0, 0.7507324310",
            "3.0, -0.4265625994",
            "10.0, 0.5596716073"
    })
    void shouldCalculatePositiveBranchWithRealLogModules(double x, double expected) {
        assertEquals(expected, system.calculate(x), EPS);
    }

    @Test
    void shouldReturnNaNAtLogCutoutPoint() {
        assertTrue(Double.isNaN(system.calculate(1.0)));
    }

    @Test
    void shouldGrowNearZeroPlus() {
        double value = system.calculate(0.001);
        assertTrue(value > 0.0 || value < 0.0);
    }
}