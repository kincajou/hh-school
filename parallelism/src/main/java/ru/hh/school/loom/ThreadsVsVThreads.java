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
  public void platformThreadCPU() throws InterruptedException {
    Thread thread = new Thread(ThreadsVsVThreads::performCPUJob);
    thread.start();
    thread.join();
  }

  @Benchmark
  public void virtualThreadCPU() throws InterruptedException {
    Thread thread = Thread.startVirtualThread(ThreadsVsVThreads::performCPUJob);
    thread.join();
  }

  @Benchmark
  public void platformThreadIO() throws InterruptedException {
    Thread thread = new Thread(ThreadsVsVThreads::performIOJob);
    thread.start();
    thread.join();
  }

  @Benchmark
  public void virtualThreadIO() throws InterruptedException {
    Thread thread = Thread.startVirtualThread(ThreadsVsVThreads::performIOJob);
    thread.join();
  }


  public static void performCPUJob() {
    Blackhole.consumeCPU(1000);
  }

  public static void performIOJob() {
    Uninterruptibles.sleepUninterruptibly(1, MILLISECONDS);
  }

}
