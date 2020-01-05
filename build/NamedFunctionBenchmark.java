import java.util.concurrent.TimeUnit;
import java.util.*;

/**
 * Represents a benchmark test for {@link NamedFunction}s.
 *
 * @author Sam Hammersley - Gonsalves
 */
public class NamedFunctionBenchmark {

    /**
     * The {@link NamedFunction} this benchmark is for.
     */
    private final NamedFunction<int[], Integer> function;

    /**
     * How many times to call the {@link #function}.
     */
    private final int executionCount;

    /**
     * How many times to repeat this benchmark.
     */
    private final int repetitions;

    /**
     * Denotes the warm-up state of the jvm (for this benchmark test).
     */
    private boolean warmedUp;

    /**
     * The default value for the number of times to execute a function.
     */
    private static final int DEFAULT_EXECUTION_COUNT = 10_000;

    /**
     * The default value for the number of times to repeat this benchmark, for average time measurements..
     */
    private static final int DEFAULT_REPETITIONS_COUNT = 100;

    /**
     * Constructs a new {@link NamedFunctionBenchmark} for the given values.
     *
     * @param function the function to be called in this test.
     * @param executionCount the number of times the given function should be applied.
     * @param repetitions the number of times to repeat the test.
     */
    public NamedFunctionBenchmark(NamedFunction<int[], Integer> function, int executionCount, int repetitions) {
        this.function = function;
        this.executionCount = executionCount;
        this.repetitions = repetitions;
    }

    public NamedFunctionBenchmark(NamedFunction<int[], Integer> function) {
        this(function, DEFAULT_EXECUTION_COUNT, DEFAULT_REPETITIONS_COUNT);
    }

    /**
     * Warms up the jvm, ensures all the necessary classes are loaded and have been
     * compiled to native code for execution (rather than java bytecode being
     * interpreted).
     */
    private void warmUp(int[] input) {
        for (int i = 0; i < DEFAULT_EXECUTION_COUNT * 10; i++) {
            function.apply(input);
        }
        warmedUp = true;
    }

    /**
     * Ensures the JVM is warmed up and then executes {@link #function} the given
     * amount of times. The average length of time the function took is calculated 
     * and then printed, along with the function name (comma seperated) to standard
     * out.
     *
     * @param input the int array input for the function.
     */
    public void runBenchmark(int...input) {
        if (!warmedUp) {
            warmUp(input);
        }

        List<Long> times = new LinkedList<>();
        for (int i = 0; i < repetitions; i++) {
            long start = System.nanoTime();

            for (int j = 0; j < executionCount; j++) {
                function.apply(input);
            }

            long delta = System.nanoTime() - start;
            times.add(delta);
        }

        double averageTime = times.stream().mapToLong(x -> x).average().getAsDouble();
        System.out.println(function.getName() + "," + averageTime);
    }

    /**
     * Invokes {@link #runBenchmark} with a psuedo-randomly generated input.
     */
    public void runBenchmarkRandomInput() {
        int[] input = new int[function.getArity()];
        for (int i = 0; i < function.getArity(); i++) {
            input[i] = (int) (Math.random() * 100);
        }

        runBenchmark(input);
    }

}
