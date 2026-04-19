package lab.pavel.function.system;

import lab.pavel.function.MathModule;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SystemFunctionWithTableStubsTest {
    private static final double EPS = 1e-6;

    @ParameterizedTest
    @MethodSource("tableData")
    void shouldCalculateSystemUsingMockitoTableStubs(TableRow row) {
        MathModule cos = mock(MathModule.class);
        MathModule sin = mock(MathModule.class);
        MathModule tan = mock(MathModule.class);
        MathModule cot = mock(MathModule.class);
        MathModule sec = mock(MathModule.class);
        MathModule csc = mock(MathModule.class);
        MathModule ln = mock(MathModule.class);
        MathModule log2 = mock(MathModule.class);
        MathModule log3 = mock(MathModule.class);
        MathModule log5 = mock(MathModule.class);
        MathModule log10 = mock(MathModule.class);

        when(cos.calculate(row.x)).thenReturn(row.cos);
        when(sin.calculate(row.x)).thenReturn(row.sin);
        when(tan.calculate(row.x)).thenReturn(row.tan);
        when(cot.calculate(row.x)).thenReturn(row.cot);
        when(sec.calculate(row.x)).thenReturn(row.sec);
        when(csc.calculate(row.x)).thenReturn(row.csc);
        when(ln.calculate(row.x)).thenReturn(row.ln);
        when(log2.calculate(row.x)).thenReturn(row.log2);
        when(log3.calculate(row.x)).thenReturn(row.log3);
        when(log5.calculate(row.x)).thenReturn(row.log5);
        when(log10.calculate(row.x)).thenReturn(row.log10);

        SystemFunction system = new SystemFunction(cos, sin, tan, cot, sec, csc, ln, log2, log3, log5, log10);

        double actual = system.calculate(row.x);
        if (Double.isNaN(row.expected)) {
            assertTrue(Double.isNaN(actual));
            return;
        }
        assertEquals(row.expected, actual, EPS);
    }

    static Stream<TableRow> tableData() {
        return Stream.of(
                new TableRow(-2.0,
                        -0.4161468365, -0.9092974268, 2.1850398633, 0.4576575544, -2.4029979617, -1.0997501703,
                        0.0, 0.0, 0.0, 0.0, 0.0,
                        0.5422491517),
                new TableRow(-1.5,
                        0.0707372017, -0.9974949866, -14.1014199472, -0.0709148443, 14.1368329030, -1.0025113042,
                        0.0, 0.0, 0.0, 0.0, 0.0,
                        0.5012209166),
                new TableRow(-0.7,
                        0.7648421873, -0.6442176872, -0.8422883805, -1.1872418321, 1.3074592597, -1.5522703270,
                        0.0, 0.0, 0.0, 0.0, 0.0,
                        -0.0000098006),
                new TableRow(0.2,
                        0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                        -1.6094379124, -2.3219280949, -1.4649735207, -1.0, -0.6989700043,
                        -1.5415272026),
                new TableRow(0.5,
                        0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                        -0.6931471806, -1.0, -0.6309297536, -0.4306765581, -0.3010299957,
                        0.7507324310),
                new TableRow(1.0,
                        1.0, 0.0, 0.0, Double.NaN, 1.0, Double.NaN,
                        0.0, 0.0, 0.0, 0.0, 0.0,
                        Double.NaN),
                new TableRow(0.0,
                        1.0, 0.0, 0.0, Double.NaN, 1.0, Double.NaN,
                        0.0, 0.0, 0.0, 0.0, 0.0,
                        Double.NaN)
        );
    }

    private record TableRow(
            double x,
            double cos,
            double sin,
            double tan,
            double cot,
            double sec,
            double csc,
            double ln,
            double log2,
            double log3,
            double log5,
            double log10,
            double expected
    ) {
    }
}