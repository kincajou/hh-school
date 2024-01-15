package ru.hh.school.loom;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.LongAdder;
import ru.hh.school.parallelism.Computation;
import ru.hh.school.parallelism.Runner;

public class LoomExecutorComputation implements Computation {

  @Override
  public long compute(int tasks) throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(tasks);
    LongAdder longAdder = new LongAdder();

    for (int i = 0; i < tasks; i++) {
      LoomRunner.VIRTUAL_THREAD_EXECUTOR.submit(() -> {
        long cpuResult = Runner.performCPUJob();
        longAdder.add(cpuResult);
        long ioResult = Runner.performIOJob();
        longAdder.add(ioResult);
        latch.countDown();
      });
    }

    latch.await();
    return longAdder.longValue();
  }
}
