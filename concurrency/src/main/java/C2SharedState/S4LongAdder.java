package C2SharedState;

import common.Task;
import static java.lang.System.currentTimeMillis;
import java.util.concurrent.atomic.LongAdder;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class S4LongAdder {

  // Java 8 added LongAdder.
  // It tries to spread one counter across several to reduce contention.
  // Still slow on heavy contention

  private static final Logger LOGGER = getLogger(S4LongAdder.class);

  static class LongAdderTask extends Task {

    LongAdderTask(int iterations) {
      super(iterations);
    }

    final LongAdder actualIterations = new LongAdder();

    @Override
    protected void onIteration() {
      actualIterations.increment();
    }
  }

  public static void main(String[] args) throws InterruptedException {

    int iterations = 100_000_000;
    int numOfThreads = 2;

    while (true) {
      long start = currentTimeMillis();

      LongAdderTask task = new LongAdderTask(iterations / numOfThreads);
      Thread thread1 = new Thread(task, "Task thread 1");
      Thread thread2 = new Thread(task, "Task thread 2");
      thread1.start();
      thread2.start();
      thread1.join();
      thread2.join();

      long duration = currentTimeMillis() - start;
      LOGGER.debug("{} ms, {} iterations", duration, task.actualIterations.intValue());

      // will it work faster or slower?
    }
  }
}
