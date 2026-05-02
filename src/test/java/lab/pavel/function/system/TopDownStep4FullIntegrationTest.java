package lab.pavel.function.system;

import lab.pavel.function.MathModule;
import lab.pavel.function.factory.ModuleFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TopDownStep4FullIntegrationTest {
    private static final double EPS_REF = 1e-4;
    private static final double EPS_CRIT = 1e-4;
    private static final double EPS_MID = 1e-5;
    private static final double EPS_POS = 1e-4;

    private final Map<String, MathModule> modules = ModuleFactory.createDefaultModules();
    private final MathModule system = modules.get("system");

    @ParameterizedTest
    @CsvSource({
            "-2.0, 0.5422491517",
            "-1.5, 0.5012209166",
            "-0.7, 0.0",
            "0.2, -1.5415272026",
            "0.5, 0.7507324310",
            "3.0, -0.4265625994"
    })
    void shouldMatchReferenceValues(double x, double expected) {
        assertEquals(expected, system.calculate(x), EPS_REF);
    }

    @ParameterizedTest
    @CsvSource({
            "-0.7, 0.0",
            "-0.72566, 0.11247",
            "-0.75181, 0.0",
            "-1.25062, 0.0",
            "-1.41257, 0.50415",
            "-1.5708, 0.5",
            "-2.3456, -7.64577",
            "-3.63012573716, 0.0",
            "-3.68047, 34.8421",
            "-3.93096, 6.89962",
            "-4.10733, 0.0",
            "-4.1336, 0.17493",
            "-4.21623, 0.0",
            "-4.71239, -0.5",
            "-5.10489, 0.0",
            "-5.616945879996, 0.0",
            "-5.77772555239, 0.0",
            "-5.78108, 0.1153",
            "-5.78438, 0.0"
    })
    void shouldMatchCriticalPoints(double x, double expected) {
        assertEquals(expected, system.calculate(x), EPS_CRIT);
    }

    @ParameterizedTest
    @CsvSource({
            "-0.68, -0.27967",
            "-0.72, 0.10728",
            "-0.74, 0.0793",
            "-0.76, -0.08622",
            "-1.2, -3.21001",
            "-1.3, 0.42163",
            "-1.5, 0.50122",
            "-2.0, 0.54225",
            "-2.3, -11.22337",
            "-2.4, -10.21249",
            "-3.62964, -0.97448",
            "-3.64337, 19.71499",
            "-3.8, 14.83784",
            "-4.0, 15.83136",
            "-4.08465, -2.0288",
            "-4.119, 0.14053",
            "-4.1695, 0.11278",
            "-4.3, -0.16995",
            "-4.9, -0.43362",
            "-5.4, 30.84044",
            "-5.76655, -2.00464",
            "-5.779, 0.07075",
            "-5.7833, 0.06347",
            "-5.79116, -1.01108"
    })
    void shouldMatchIntermediatePoints(double x, double expected) {
        assertEquals(expected, system.calculate(x), EPS_MID);
    }

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
    void shouldMatchPositiveBranchPoints(double x, double expected) {
        assertEquals(expected, system.calculate(x), EPS_POS);
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