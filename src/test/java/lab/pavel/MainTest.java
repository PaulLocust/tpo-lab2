package lab.pavel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {

    @TempDir
    Path tempDir;

    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void shouldInstantiateMainClass() {
        assertNotNull(new Main());
    }

    @Test
    void shouldGenerateCsvForSingleModule() {
        Path out = tempDir.resolve("cos.csv");
        Main.main(new String[]{"single", "cos", "-1", "1", "0.2", out.toString(), ";"});
        assertTrue(Files.exists(out));
    }

    @Test
    void shouldGenerateCsvForAllModules() {
        Main.main(new String[]{"all", "-1", "1", "0.5", tempDir.toString(), ";"});
        assertTrue(Files.exists(tempDir.resolve("system.csv")));
        assertTrue(Files.exists(tempDir.resolve("cos.csv")));
        assertTrue(Files.exists(tempDir.resolve("ln.csv")));
    }

    @Test
    void shouldUseDefaultDelimiterForSingleAndAll() {
        Path singleOut = tempDir.resolve("single-default.csv");
        Main.main(new String[]{"single", "cos", "-1", "1", "0.5", singleOut.toString()});
        assertTrue(Files.exists(singleOut));

        Main.main(new String[]{"all", "-1", "1", "1", tempDir.toString()});
        assertTrue(Files.exists(tempDir.resolve("system.csv")));
    }

    @Test
    void shouldPrintUsageWhenNoArgsOrUnknownModeOrNotEnoughArgs() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Main.main(new String[]{});
        Main.main(new String[]{"unknown-mode"});
        Main.main(new String[]{"single", "cos", "0", "1", "0.1"});
        Main.main(new String[]{"all", "0", "1", "0.1"});

        String printed = out.toString();
        assertTrue(printed.contains("Usage:"));
        assertTrue(printed.contains("single <module>"));
        assertTrue(printed.contains("all <from> <to> <step> <outputDir>"));
    }

    @Test
    void shouldPrintErrorForUnknownModule() {
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        System.setErr(new PrintStream(err));

        Path out = tempDir.resolve("bad.csv");
        Main.main(new String[]{"single", "unknown", "-1", "1", "0.1", out.toString()});

        assertTrue(err.toString().contains("Unknown module"));
        assertFalse(Files.exists(out));
    }
}