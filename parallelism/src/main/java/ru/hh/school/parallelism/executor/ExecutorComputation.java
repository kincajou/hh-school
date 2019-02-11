package ru.hh.school.parallelism.executor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;
import ru.hh.school.parallelism.Computation;
import ru.hh.school.parallelism.Runner;

public class ExecutorComputation implements Computation {

  public long compute(int tasks) throws InterruptedException {
    ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    CountDownLatch latch = new CountDownLatch(tasks);
    LongAdder longAdder = new LongAdder();

    for (int i = 0; i < tasks; i++) {
      executor.submit(() -> {
        long cpuResult = Runner.performCPUJob();
        longAdder.add(cpuResult);
        long ioResult = Runner.performIOJob();
        longAdder.add(ioResult);
        latch.countDown();
      });
    }

    latch.await();
    executor.shutdown();
    return longAdder.longValue();
  }
}
