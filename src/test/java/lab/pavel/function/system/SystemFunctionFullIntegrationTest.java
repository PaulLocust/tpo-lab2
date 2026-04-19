package lab.pavel.function.system;

import lab.pavel.function.MathModule;
import lab.pavel.function.factory.ModuleFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SystemFunctionFullIntegrationTest {
    private static final double EPS = 1e-4;

    private final Map<String, MathModule> modules = ModuleFactory.createDefaultModules();
    private final MathModule system = modules.get("system");

    @ParameterizedTest
    @CsvSource({
            "-2.0, 0.5422491517",
            "-1.5, 0.5012209166",
            "-0.7, -0.0000098006",
            "0.2, -1.5415272026",
            "0.5, 0.7507324310",
            "3.0, -0.4265625994"
    })
    void shouldMatchReferenceValues(double x, double expected) {
        assertEquals(expected, system.calculate(x), EPS);
    }

    @Test
    void shouldKeepPeriodicityOnNegativeTrigBranch() {
        double x = -0.7;
        double period = 2.0 * Math.PI;
        assertEquals(system.calculate(x), system.calculate(x - period), 1e-4);
    }

    @Test
    void shouldReturnNaNAtUndefinedPoints() {
        assertTrue(Double.isNaN(system.calculate(0.0)));
        assertTrue(Double.isNaN(system.calculate(1.0)));
    }
}