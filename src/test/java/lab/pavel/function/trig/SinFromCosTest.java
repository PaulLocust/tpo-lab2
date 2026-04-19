package lab.pavel.function.trig;

import lab.pavel.function.MathModule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SinFromCosTest {

    @Test
    void shouldCalculateSinViaCosDependency() {
        MathModule cos = mock(MathModule.class);
        SinFromCos sinFromCos = new SinFromCos(cos);

        double x = -0.7;
        double shifted = Math.PI / 2.0 - x;

        when(cos.calculate(shifted)).thenReturn(-0.6442176872);

        assertEquals(-0.6442176872, sinFromCos.calculate(x), 1e-12);
        verify(cos).calculate(shifted);
    }
}