package ru.hh.school.loom;

import com.google.common.util.concurrent.Uninterruptibles;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@Fork(value = 1, warmups = 0)
@Warmup(iterations = 1, time = 5)
@Measurement(iterations = 3, time = 5)
@BenchmarkMode(Mode.Throughput)
public class ThreadsVsVThreads {

  public static void main(String[] args) throws Exception {
    new Runner(new OptionsBuilder().include(ThreadsVsVThreads.class.getSimpleName()).forks(1).build()).run();
  }

  @Benchmark
  public void platformThread() throws InterruptedException {
    Thread thread = new Thread(ThreadsVsVThreads::performCPUJob);
    thread.start();
    thread.join();
  }

  @Benchmark
  public void virtualThread() throws InterruptedException {
    Thread thread = Thread.startVirtualThread(ThreadsVsVThreads::performCPUJob);
    thread.join();
  }

  public static long performCPUJob() {
    Blackhole.consumeCPU(1000);
    return 100;
  }

  public static long performIOJob() {
    Uninterruptibles.sleepUninterruptibly(1, MILLISECONDS);
    return 200;
  }

}
