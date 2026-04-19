package lab.pavel.io;

import lab.pavel.function.MathModule;
import lab.pavel.function.factory.ModuleFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CsvExporterTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldExportSingleModuleToCsv() throws IOException {
        MathModule module = x -> x * x;
        Path output = tempDir.resolve("square.csv");

        CsvExporter.export(module, 0.0, 1.0, 0.5, output, ';');

        List<String> lines = Files.readAllLines(output);
        assertEquals("X;Result", lines.get(0));
        assertTrue(lines.size() >= 3);
    }

    @Test
    void shouldExportAllDefaultModules() throws IOException {
        Map<String, MathModule> modules = ModuleFactory.createDefaultModules();

        for (Map.Entry<String, MathModule> entry : modules.entrySet()) {
            Path output = tempDir.resolve(entry.getKey() + ".csv");
            CsvExporter.export(entry.getValue(), -1.0, 1.0, 0.5, output, ';');
            assertTrue(Files.exists(output));
            assertTrue(Files.size(output) > 0);
        }
    }

    @Test
    void shouldExportWithNegativeStepForDecreasingRange() throws IOException {
        MathModule module = x -> x;
        Path output = tempDir.resolve("decreasing.csv");

        CsvExporter.export(module, 1.0, -1.0, -0.5, output, ';');

        List<String> lines = Files.readAllLines(output);
        assertEquals("X;Result", lines.get(0));
        assertTrue(lines.size() >= 3);
    }

    @Test
    void shouldRejectZeroStep() {
        MathModule module = x -> x;
        Path output = tempDir.resolve("zero-step.csv");

        assertThrows(IllegalArgumentException.class,
                () -> CsvExporter.export(module, 0.0, 1.0, 0.0, output, ';'));
    }

    @Test
    void shouldRejectNegativeStepForIncreasingRange() {
        MathModule module = x -> x;
        Path output = tempDir.resolve("wrong-step-1.csv");

        assertThrows(IllegalArgumentException.class,
                () -> CsvExporter.export(module, -1.0, 1.0, -0.5, output, ';'));
    }

    @Test
    void shouldRejectPositiveStepForDecreasingRange() {
        MathModule module = x -> x;
        Path output = tempDir.resolve("wrong-step-2.csv");

        assertThrows(IllegalArgumentException.class,
                () -> CsvExporter.export(module, 1.0, -1.0, 0.5, output, ';'));
    }
}