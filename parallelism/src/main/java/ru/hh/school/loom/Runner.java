package ru.hh.school.loom;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.infra.Blackhole;

// have to add jvm argument:
// --add-modules=jdk.incubator.concurrent
@State(Scope.Benchmark)
public class Runner extends ru.hh.school.parallelism.Runner {

  static ExecutorService VIRTUAL_THREAD_EXECUTOR = Executors.newVirtualThreadPerTaskExecutor();

  @TearDown
  public void tearDown() {
    VIRTUAL_THREAD_EXECUTOR.shutdown(); // not sure if needed
  }

  @Benchmark
  public void loomExecutor(Blackhole blackhole) throws InterruptedException {
    blackhole.consume(new LoomExecutorComputation().compute(CYCLES));
  }

  @Benchmark
  public void loomForkJoin(Blackhole blackhole) throws InterruptedException, ExecutionException {
    blackhole.consume(new LoomForkJoinComputation().compute(CYCLES));
  }

}
