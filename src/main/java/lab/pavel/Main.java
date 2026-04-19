package lab.pavel;

import lab.pavel.function.MathModule;
import lab.pavel.function.factory.ModuleFactory;
import lab.pavel.io.CsvExporter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                printUsage();
                return;
            }

            Map<String, MathModule> modules = ModuleFactory.createDefaultModules();
            String mode = args[0].toLowerCase();

            if ("single".equals(mode)) {
                exportSingleModule(args, modules);
                return;
            }

            if ("all".equals(mode)) {
                exportAllModules(args, modules);
                return;
            }

            printUsage();
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }

    private static void exportSingleModule(String[] args, Map<String, MathModule> modules) throws IOException {
        if (args.length < 6) {
            printUsage();
            return;
        }

        String moduleName = args[1].toLowerCase();
        MathModule module = modules.get(moduleName);
        if (module == null) {
            throw new IllegalArgumentException("Unknown module: " + moduleName);
        }

        double from = Double.parseDouble(args[2]);
        double to = Double.parseDouble(args[3]);
        double step = Double.parseDouble(args[4]);
        Path output = Path.of(args[5]);
        char delimiter = args.length > 6 ? args[6].charAt(0) : ';';

        CsvExporter.export(module, from, to, step, output, delimiter);
        System.out.println("CSV generated: " + output);
    }

    private static void exportAllModules(String[] args, Map<String, MathModule> modules) throws IOException {
        if (args.length < 5) {
            printUsage();
            return;
        }

        double from = Double.parseDouble(args[1]);
        double to = Double.parseDouble(args[2]);
        double step = Double.parseDouble(args[3]);
        Path outputDir = Path.of(args[4]);
        char delimiter = args.length > 5 ? args[5].charAt(0) : ';';

        for (Map.Entry<String, MathModule> entry : modules.entrySet()) {
            Path output = outputDir.resolve(entry.getKey() + ".csv");
            CsvExporter.export(entry.getValue(), from, to, step, output, delimiter);
        }
        System.out.println("CSV generated for all modules in: " + outputDir);
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  single <module> <from> <to> <step> <outputCsv> [delimiter]");
        System.out.println("  all <from> <to> <step> <outputDir> [delimiter]");
        System.out.println("Modules: cos, sin, tan, cot, sec, csc, ln, log_2, log_3, log_5, log_10, system");
    }
}