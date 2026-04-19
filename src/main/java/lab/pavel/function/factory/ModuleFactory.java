package lab.pavel.function.factory;

import lab.pavel.function.MathModule;
import lab.pavel.function.log.LnSeries;
import lab.pavel.function.log.LogBaseModule;
import lab.pavel.function.system.SystemFunction;
import lab.pavel.function.trig.CosSeries;
import lab.pavel.function.trig.CotModule;
import lab.pavel.function.trig.CscModule;
import lab.pavel.function.trig.SecModule;
import lab.pavel.function.trig.SinFromCos;
import lab.pavel.function.trig.TanModule;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ModuleFactory {
    private ModuleFactory() {
    }

    public static Map<String, MathModule> createDefaultModules() {
        MathModule cos = new CosSeries(1e-12, 100_000);
        MathModule sin = new SinFromCos(cos);
        MathModule tan = new TanModule(sin, cos);
        MathModule cot = new CotModule(sin, cos);
        MathModule sec = new SecModule(cos);
        MathModule csc = new CscModule(sin);

        MathModule ln = new LnSeries(1e-12, 500_000);
        MathModule log2 = new LogBaseModule(2.0, ln);
        MathModule log3 = new LogBaseModule(3.0, ln);
        MathModule log5 = new LogBaseModule(5.0, ln);
        MathModule log10 = new LogBaseModule(10.0, ln);

        MathModule system = new SystemFunction(
                cos,
                sin,
                tan,
                cot,
                sec,
                csc,
                ln,
                log2,
                log3,
                log5,
                log10
        );

        Map<String, MathModule> modules = new LinkedHashMap<>();
        modules.put("cos", cos);
        modules.put("sin", sin);
        modules.put("tan", tan);
        modules.put("cot", cot);
        modules.put("sec", sec);
        modules.put("csc", csc);
        modules.put("ln", ln);
        modules.put("log_2", log2);
        modules.put("log_3", log3);
        modules.put("log_5", log5);
        modules.put("log_10", log10);
        modules.put("system", system);
        return modules;
    }
}