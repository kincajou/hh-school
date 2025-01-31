package C2SharedState;

import common.Task;
import static java.lang.System.currentTimeMillis;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class S3Atomic {

  // AtomicInteger uses Compare and Swap (CAS)
  // Roughly:

  //  for (;;) {
  //    int current = get();
  //    int next = current + 1;
  //    if (hardware.compareAndSwap(current, next))
  //      return next;
  //  }
  //
  // Great on low contented threads, but bad on high.

  private static final Logger LOGGER = getLogger(S3Atomic.class);

  static class AtomicTask extends Task {

    AtomicTask(int iterations) {
      super(iterations);
    }

    // different types of Atomic exist
    final AtomicInteger actualIterations = new AtomicInteger();

    // same as synchronized block
    @Override
    protected void onIteration() {
      actualIterations.incrementAndGet();
    }
  }

  public static void main(String[] args) throws InterruptedException {

    int iterations = 100_000_000;
    int numOfThreads = 2;

    while (true) {
      long start = currentTimeMillis();

      AtomicTask task = new AtomicTask(iterations / numOfThreads);
      Thread thread1 = new Thread(task, "Task thread 1");
      Thread thread2 = new Thread(task, "Task thread 2");
      thread1.start();
      thread2.start();
      thread1.join();
      thread2.join();

      long duration = currentTimeMillis() - start;
      LOGGER.debug("{} ms, {} iterations", duration, task.actualIterations.get());

      // will this work better or worse than synchronized block?
    }
  }
}
