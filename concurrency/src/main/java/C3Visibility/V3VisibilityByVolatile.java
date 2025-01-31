package C3Visibility;

import common.Task;
import common.Utils;
import static java.lang.System.currentTimeMillis;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class V3VisibilityByVolatile {

  private static final Logger LOGGER = getLogger(V3VisibilityByVolatile.class);

  // Volatile guarantees visibility.
  // It is cheaper that synchronized.
  // Should we always use volatile?

  static class VolatileTask extends Task {

    VolatileTask(int iterations) {
      super(iterations);
    }

    // Thread that reads will see correct value after another thread writes it
    volatile int actualIterations = 0;

    @Override
    protected void onIteration() {
      actualIterations++;
    }
  }

  public static void main(String[] args) throws InterruptedException {

    int iterations = 100_000_000;

    while (true) {
      long start = currentTimeMillis();

      VolatileTask task = new VolatileTask(iterations);
      Thread thread = new Thread(task, "Task thread");
      thread.start();
      while (true) {
        if (task.actualIterations >= iterations) {
          break;
        }
        Utils.consumeCPUWithoutBarrier(100);
      }

      long duration = currentTimeMillis() - start;
      LOGGER.debug("{} ms, value: {}", duration, task.actualIterations);
    }
  }
}
