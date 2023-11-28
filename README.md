This repository features variants of 2x2, 3x3 & 4x4 float matrices with decently optimized implementations. Most loops are manually unrolled, because especially in larger matrices, inlining limits sometimes prevented index calculations from being constant folded by the JIT compiler. JMH benchmarks and JUnit tests are provided.

### Array-backed
- 👍 Fast random access via coordinate linearization
- 👍 Fast array access for off-heap transfer
- 👎 Additional indirection*

<sub>*May lead to more frequent cache misses in real-world applications</sub>

### Field-backed
- 👎 Slower random access due to jump table
- 👎 Slower array access
- 👍 Faster multiplication*

<sub>*Cause to be determined, assumed to be due to higher locality</sub>

## Results
🖥️ Ryzen 5 3600 @ 4 GHz / 16 GB Corsair Vengeance Pro DDR4-3200 CL16 @ 1066 MHz / Win11

📩 Feel free to run the package and submit an issue with your own results and hardware details.

> [!WARNING]
> Keep in mind that microbenchmarks are not necessarily representative of performance in real-world applications! Access patterns, caches and JVM settings can have a major impact on relative performance.
```
# JMH version: 1.35
# VM version: JDK 21.0.1, OpenJDK 64-Bit Server VM, 21.0.1+12-29
# VM options: <none>
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 5 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op

Benchmark                    (variant)  Mode  Cnt   Score   Error  Units
MatrixBenchmark.array               A2  avgt   10   1,078 ± 0,021  ns/op
MatrixBenchmark.array               F2  avgt   10   3,706 ± 0,133  ns/op
MatrixBenchmark.array               A3  avgt   10   1,086 ± 0,010  ns/op
MatrixBenchmark.array               F3  avgt   10   6,200 ± 0,089  ns/op
MatrixBenchmark.array               A4  avgt   10   1,069 ± 0,013  ns/op
MatrixBenchmark.array               F4  avgt   10   9,466 ± 0,450  ns/op
MatrixBenchmark.determinant         A2  avgt   10   1,780 ± 0,023  ns/op
MatrixBenchmark.determinant         F2  avgt   10   1,240 ± 0,021  ns/op
MatrixBenchmark.determinant         A3  avgt   10   2,836 ± 0,059  ns/op
MatrixBenchmark.determinant         F3  avgt   10   2,197 ± 0,060  ns/op
MatrixBenchmark.determinant         A4  avgt   10   8,140 ± 0,234  ns/op
MatrixBenchmark.determinant         F4  avgt   10   7,645 ± 0,220  ns/op
MatrixBenchmark.invert              A2  avgt   10   7,516 ± 0,116  ns/op
MatrixBenchmark.invert              F2  avgt   10   7,540 ± 0,157  ns/op
MatrixBenchmark.invert              A3  avgt   10  11,456 ± 0,060  ns/op
MatrixBenchmark.invert              F3  avgt   10  13,051 ± 0,098  ns/op
MatrixBenchmark.invert              A4  avgt   10  30,944 ± 0,569  ns/op
MatrixBenchmark.invert              F4  avgt   10  29,177 ± 1,213  ns/op
MatrixBenchmark.multiply            A2  avgt   10   7,056 ± 0,083  ns/op
MatrixBenchmark.multiply            F2  avgt   10   4,070 ± 0,258  ns/op
MatrixBenchmark.multiply            A3  avgt   10  15,581 ± 0,315  ns/op
MatrixBenchmark.multiply            F3  avgt   10   7,028 ± 0,324  ns/op
MatrixBenchmark.multiply            A4  avgt   10  26,696 ± 1,442  ns/op
MatrixBenchmark.multiply            F4  avgt   10  17,949 ± 0,495  ns/op
MatrixBenchmark.scale               A2  avgt   10   2,764 ± 0,020  ns/op
MatrixBenchmark.scale               F2  avgt   10   2,729 ± 0,024  ns/op
MatrixBenchmark.scale               A3  avgt   10   4,411 ± 0,052  ns/op
MatrixBenchmark.scale               F3  avgt   10   3,107 ± 0,179  ns/op
MatrixBenchmark.scale               A4  avgt   10   4,957 ± 0,415  ns/op
MatrixBenchmark.scale               F4  avgt   10   4,433 ± 0,041  ns/op
MatrixBenchmark.set                 A2  avgt   10   4,600 ± 0,040  ns/op
MatrixBenchmark.set                 F2  avgt   10  12,930 ± 0,141  ns/op
MatrixBenchmark.set                 A3  avgt   10  13,799 ± 0,184  ns/op
MatrixBenchmark.set                 F3  avgt   10  27,308 ± 2,289  ns/op
MatrixBenchmark.set                 A4  avgt   10   4,594 ± 0,058  ns/op
MatrixBenchmark.set                 F4  avgt   10  18,311 ± 0,190  ns/op
MatrixBenchmark.transpose           A2  avgt   10   2,014 ± 0,013  ns/op
MatrixBenchmark.transpose           F2  avgt   10   1,993 ± 0,007  ns/op
MatrixBenchmark.transpose           A3  avgt   10   3,310 ± 0,098  ns/op
MatrixBenchmark.transpose           F3  avgt   10   2,452 ± 0,042  ns/op
MatrixBenchmark.transpose           A4  avgt   10   5,805 ± 0,049  ns/op
MatrixBenchmark.transpose           F4  avgt   10   3,765 ± 0,012  ns/op
```