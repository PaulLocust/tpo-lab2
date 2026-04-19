package lab.pavel.io;

import lab.pavel.function.MathModule;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public final class CsvExporter {
    private CsvExporter() {
    }

    public static void export(MathModule module, double from, double to, double step, Path output, char delimiter)
            throws IOException {
        if (step == 0.0) {
            throw new IllegalArgumentException("Step cannot be 0");
        }
        if (from < to && step < 0.0) {
            throw new IllegalArgumentException("Step must be positive for increasing range");
        }
        if (from > to && step > 0.0) {
            throw new IllegalArgumentException("Step must be negative for decreasing range");
        }

        Files.createDirectories(output.getParent());
        try (Writer writer = Files.newBufferedWriter(output)) {
            writer.write("X" + delimiter + "Result\n");

            if (step > 0.0) {
                for (double x = from; x <= to + 1e-12; x += step) {
                    writeRow(writer, x, module.calculate(x), delimiter);
                }
            } else {
                for (double x = from; x >= to - 1e-12; x += step) {
                    writeRow(writer, x, module.calculate(x), delimiter);
                }
            }
        }
    }

    private static void writeRow(Writer writer, double x, double value, char delimiter) throws IOException {
        String row = String.format(Locale.US, "%.10f%c%.10f%n", x, delimiter, value);
        writer.write(row);
    }
}