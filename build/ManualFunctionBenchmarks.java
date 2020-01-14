import java.util.function.*;
import java.util.*;
import java.lang.reflect.*;

public class ManualFunctionBenchmarks {

    static Integer fact(int[] input) {
        int n = input[0];
        int product = 1;
        for (int j = 1; j <= n; j++) {
            product *= j;
        }
        return product;
    }

    final static Function<int[], Integer> EQUALS = x -> x[0] == x[1] ? 1 : 0;

    final static Function<int[], Integer> FACTORIAL = ManualFunctionBenchmarks::fact;

    final static Function<int[], Integer> IFELSE = x -> x[2] == 1 ? x[0] : x[1];

    final static Function<int[], Integer> LESSTHAN = x -> x[0] < x[1] ? 1 : 0;

    final static Function<int[], Integer> MAX = x -> x[0] > x[1] ? x[0] : x[1];

    final static Function<int[], Integer> MIN = x -> x[0] < x[1] ? x[0] : x[1];

    final static Function<int[], Integer> MODULO = x -> x[0] % x[1];

    final static Function<int[], Integer> MORETHAN = x -> x[0] > x[1] ? 1 : 0;

    final static Function<int[], Integer> MULTIPLY = x -> x[0] * x[1];

    final static Function<int[], Integer> QUOTIENT = x -> x[0] / x[1];

    final static Function<int[], Integer> SUBTRACT = x -> x[0] - x[1];

    final static Function<int[], Integer> SUM = x -> x[0] + x[1];

    final static Function<int[], Integer> TRI = x -> x[0] * (x[0] + 1) / 2;

    private final static Map<String, Function<int[], Integer>> FUNCS = new LinkedHashMap<>();

    static {
        try {
            Field[] fields = ManualFunctionBenchmarks.class.getDeclaredFields();

            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers())
                     && !field.getName().equals("FUNCS")
                     && !field.getName().equals("WARMUP_EXECUTIONS")) {
                    Function<int[], Integer> func = (Function<int[], Integer>) field.get(null);

                    FUNCS.put(field.getName().toLowerCase(), func);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final int WARMUP_EXECUTIONS = 20_000;

    public static void main(String[] args) {
        int[] input = {
            1 + (int) (99 * Math.random()),
            1 + (int) (99 * Math.random()),
            1
        };

        int executionCount = Integer.parseInt(args[0]);

        if (args.length > 1) {
            Function<int[], Integer> func = FUNCS.get(args[1]);

            // warm up
            for (int i = 0; i < WARMUP_EXECUTIONS; i++) {
                func.apply(input);
            }

            double averageTime = doBenchmark(func, input, executionCount);
            System.out.println(args[1] + "," + averageTime);
        } else {
            for (Map.Entry<String, Function<int[], Integer>> entry : FUNCS.entrySet()) {
                Function<int[], Integer> func = entry.getValue();

                // warm up
                for (int i = 0; i < WARMUP_EXECUTIONS; i++) {
                    func.apply(input);
                }

                double averageTime = doBenchmark(func, input, executionCount);
                System.out.println(entry.getKey() + "," + averageTime);
            }
        }
    }

    static double doBenchmark(Function<int[], Integer> func, int[] input, int executionCount) {
        double averageTime = 0;
        for (int i = 0; i < executionCount; i++) {
            long start = System.nanoTime();
            func.apply(input);
            long delta = System.nanoTime() - start;

            averageTime = ((averageTime * i) + delta) / (i + 1);
        }
        return averageTime;
    }

}
