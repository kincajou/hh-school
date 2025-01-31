package C3Visibility;

import common.Task;
import common.Utils;
import static java.lang.System.currentTimeMillis;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class V2VisibilityBySynchronized {

  private static final Logger LOGGER = getLogger(V2VisibilityBySynchronized.class);

  // Synchronized guarantees visibility of ALL variables (incl. i.e. blackhole) between
  // threads that synchronize on the same monitor.
  // But it prevents parallel execution and is not cheap.

  static class SynchronizedTask extends Task {

    SynchronizedTask(int iterations) {
      super(iterations);
    }

    final Object monitor = new Object();
    int actualIterations = 0;

    @Override
    protected void onIteration() {
      synchronized (monitor) {
        actualIterations++;
      }
    }
  }

  public static void main(String[] args) throws InterruptedException {

    int iterations = 100_000_000;

    while (true) {
      long start = currentTimeMillis();

      SynchronizedTask task = new SynchronizedTask(iterations);
      Thread thread = new Thread(task, "Task thread");
      thread.start();
      while (true) {
        synchronized (task.monitor) {
          if (task.actualIterations >= iterations) {
            break;
          }
        }
        Utils.consumeCPUWithoutBarrier(100);
      }

      long duration = currentTimeMillis() - start;
      LOGGER.debug("{} ms, value: {}", duration, task.actualIterations);
      System.gc();
    }
  }
}
