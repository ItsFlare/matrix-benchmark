package net.durchholz.matbench;

import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

@BenchmarkMode({Mode.AverageTime})
@Warmup(iterations = 2)
@Fork(value = 2, warmups = 0)
@Measurement(iterations = 5)
@OutputTimeUnit(value = NANOSECONDS)
public class MatrixBenchmark {

    public static void main(String[] args) throws IOException {
        Main.main(new String[]{MatrixBenchmark.class.getSimpleName(), "-gc", "true"});
    }

    @State(Scope.Thread)
    public static class BenchmarkState {

        public ThreadLocalRandom random;

        public Matrix matrix;
        public int dimension;

        @Param({"A2", "F2", "A3", "F3", "A4", "F4"})
        public String variant;

        public BenchmarkState() {}

        @Setup
        public void init() {
            random = ThreadLocalRandom.current();

            matrix = switch (variant) {
                case "A2" -> new ArrayMatrix2f();
                case "A3" -> new ArrayMatrix3f();
                case "A4" -> new ArrayMatrix4f();
                case "F2" -> new FieldMatrix2f();
                case "F3" -> new FieldMatrix3f();
                case "F4" -> new FieldMatrix4f();
                default -> throw new IllegalStateException("Unexpected value: " + variant);
            };

            dimension = matrix.dimension();

            //Generate random invertible matrix
            do {
                for (int i = 0; i < matrix.dimension(); i++) {
                    for (int j = 0; j < matrix.dimension(); j++) {
                        matrix.set(i, j, ThreadLocalRandom.current().nextFloat(0.4f) + 0.5f);
                    }
                }
            } while (Math.abs(matrix.determinant()) == 0);
        }
    }

    @Benchmark
    public void set(BenchmarkState state, Blackhole blackhole) {
        blackhole.consume(state.matrix.get(state.random.nextInt(state.dimension), state.random.nextInt(state.dimension)));
    }

    @Benchmark
    public void scale(BenchmarkState state) {
        state.matrix.scale(1.01f);
    }

    @Benchmark
    public void determinant(BenchmarkState state, Blackhole blackhole) {
        blackhole.consume(state.matrix.determinant());
    }

    @Benchmark
    public void invert(BenchmarkState state) {
        state.matrix.invert();
    }

    @Benchmark
    public void transpose(BenchmarkState state) {
        state.matrix.transpose();
    }

    @Benchmark
    public void array(BenchmarkState state, Blackhole blackhole) {
        blackhole.consume(state.matrix.array());
    }

    @Benchmark
    public void multiply(BenchmarkState state, Blackhole blackhole) {
        blackhole.consume(Matrix.multiply(state.matrix, state.matrix));
    }
}