package C1Threads;

import common.Task;
import static java.lang.System.currentTimeMillis;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class T1OneThread {

  // One (main) thread does some work.
  // In this case - calculates something.
  // Not useful enough. But pretend - it is)
  // Uses only one CPU core.
  // How can we use more cores?

  private static final Logger LOGGER = getLogger(T1OneThread.class);

  static class UselessTask extends Task {

    private final int iterations;

    UselessTask(int iterations) {
      super(iterations, false);
      this.iterations = iterations;
    }

    @Override
    protected void onIteration() {
      int t = 0;
      for (int i = 0; i < iterations; i++) {
        t += t * 2521;
      }
    }
  }

  public static void main(String[] args) {

    int iterations = 1_000_000_000;

    while (true) {
      long start = currentTimeMillis();

      UselessTask task = new UselessTask(iterations);
      task.run();

      long duration = currentTimeMillis() - start;
      LOGGER.debug("{} ms", duration);
    }

  }
}
