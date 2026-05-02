package lab.pavel.function.system;

import lab.pavel.function.MathModule;
import lab.pavel.function.log.LnSeries;
import lab.pavel.function.log.LogBaseModule;
import lab.pavel.function.trig.CotModule;
import lab.pavel.function.trig.CscModule;
import lab.pavel.function.trig.SecModule;
import lab.pavel.function.trig.SinFromCos;
import lab.pavel.function.trig.TanModule;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TopDownStep3Level2RealTest {
    private static final double EPS = 1e-4;

    @ParameterizedTest
    @MethodSource("tableData")
    void shouldCalculateWithLevel1_2RealAndLevel3Mocked(TableRow row) {

        MathModule cos = mock(MathModule.class);

        when(cos.calculate(eq(row.x))).thenReturn(row.cos);

        double shiftedX = Math.PI / 2.0 - row.x;
        when(cos.calculate(eq(shiftedX))).thenReturn(row.cosShifted);

        MathModule sin = new SinFromCos(cos);
        MathModule csc = new CscModule(sin);
        MathModule ln = new LnSeries(1e-12, 100_000);

        MathModule tan = new TanModule(sin, cos);
        MathModule cot = new CotModule(sin, cos);
        MathModule sec = new SecModule(cos);
        MathModule log2 = new LogBaseModule(2.0, ln);
        MathModule log3 = new LogBaseModule(3.0, ln);
        MathModule log5 = new LogBaseModule(5.0, ln);
        MathModule log10 = new LogBaseModule(10.0, ln);

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
                new TableRow(-2.0, -0.4161468365, -0.9092974268, 0.5422491517),
                new TableRow(-1.5, 0.0707372017, -0.9974949866, 0.5012209166),
                new TableRow(-0.7, 0.7648421873, -0.6442176872, -0.0000098006),
                new TableRow(0.2, 0.0, 0.0, -1.5415272026),
                new TableRow(0.5, 0.0, 0.0, 0.7507324310),
                new TableRow(1.0, 1.0, 0.0, Double.NaN),
                new TableRow(0.0, 1.0, 0.0, Double.NaN)
        );
    }

    private record TableRow(
            double x,
            double cos,
            double cosShifted, // this is effectively sin(x)
            double expected
    ) {
    }
}