package lab.pavel.function.log;

import lab.pavel.function.MathModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LogBaseModuleTest {
    private static final double EPS = 1e-6;

    private final LnSeries lnSeries = new LnSeries(1e-12, 500_000);

    @ParameterizedTest
    @CsvSource({
            "2.0, 8.0, 3.0",
            "3.0, 9.0, 2.0",
            "5.0, 25.0, 2.0",
            "10.0, 0.1, -1.0"
    })
    void shouldCalculateLogByBase(double base, double x, double expected) {
        LogBaseModule logModule = new LogBaseModule(base, lnSeries);
        assertEquals(expected, logModule.calculate(x), EPS);
    }

    @Test
    void shouldReturnNaNOutsideDomain() {
        LogBaseModule log2 = new LogBaseModule(2.0, lnSeries);
        assertTrue(Double.isNaN(log2.calculate(0.0)));
        assertTrue(Double.isNaN(log2.calculate(-3.0)));
    }

    @ParameterizedTest
    @CsvSource({"-2.0", "0.0", "1.0"})
    void shouldRejectInvalidBase(double base) {
        assertThrows(IllegalArgumentException.class, () -> new LogBaseModule(base, lnSeries));
    }

    @Test
    void shouldReturnNaNWhenLnBaseIsNaN() {
        MathModule lnMock = mock(MathModule.class);
        when(lnMock.calculate(2.0)).thenReturn(Double.NaN);
        when(lnMock.calculate(3.0)).thenReturn(1.0);

        LogBaseModule log2 = new LogBaseModule(2.0, lnMock);
        assertTrue(Double.isNaN(log2.calculate(3.0)));
    }

    @Test
    void shouldReturnNaNWhenLnBaseIsTooSmall() {
        MathModule lnMock = mock(MathModule.class);
        when(lnMock.calculate(2.0)).thenReturn(0.0);
        when(lnMock.calculate(3.0)).thenReturn(1.0);

        LogBaseModule log2 = new LogBaseModule(2.0, lnMock);
        assertTrue(Double.isNaN(log2.calculate(3.0)));
    }
}