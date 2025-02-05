package ru.hh.school.loom;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Benchmark)
public class Loom6Benchmark extends ru.hh.school.parallelism.Runner {

  static ExecutorService VIRTUAL_THREAD_EXECUTOR = Executors.newVirtualThreadPerTaskExecutor();

  @TearDown
  public void myTearDown() {
    VIRTUAL_THREAD_EXECUTOR.shutdown(); // not sure if needed
  }

  public static void main(String[] args) throws Exception {
    new org.openjdk.jmh.runner.Runner(new OptionsBuilder().include(Loom6Benchmark.class.getCanonicalName()).addProfiler("gc").forks(1).build()).run();
  }

  // both loom benchmarks are behind the rest on default settings due to high gc utilization
  // but perform much better on IO-bound operations - set ru.hh.school.parallelism.Runner.IO_MILLISECONDS to non zero

  @Benchmark
  public void loomExecutor(Blackhole blackhole) throws InterruptedException {
    blackhole.consume(new LoomExecutorComputation().compute(CYCLES));
  }

  @Benchmark
  public void loomForkJoin(Blackhole blackhole) throws InterruptedException, ExecutionException {
    blackhole.consume(new LoomForkJoinComputation().compute(CYCLES));
  }
}
