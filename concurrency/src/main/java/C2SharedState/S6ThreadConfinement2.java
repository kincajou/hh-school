package C2SharedState;

import common.Task;
import static java.lang.System.currentTimeMillis;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class S6ThreadConfinement2 {

  // Uses Task that prevents loop unrolling.
  // -XX:+PrintCompilation
  // https://stackoverflow.com/a/41154126

  private static final Logger LOGGER = getLogger(S6ThreadConfinement2.class);

  static class ThreadConfinementTask extends Task {

    ThreadConfinementTask(int iterations) {
      super(iterations);
    }

    int actualIterations;

    @Override
    protected void onIteration() {
      actualIterations++;
    }
  }

  public static void main(String[] args) throws InterruptedException {

    int iterations = 100_000_000;
    int numOfThreads = 2;

    while (true) {
      long start = currentTimeMillis();

      ThreadConfinementTask task1 = new ThreadConfinementTask(iterations / numOfThreads);
      ThreadConfinementTask task2 = new ThreadConfinementTask(iterations / numOfThreads);
      Thread thread1 = new Thread(task1, "Task thread 1");
      Thread thread2 = new Thread(task2, "Task thread 2");
      thread1.start();
      thread2.start();
      thread1.join();
      thread2.join();
      int actualIterations = task1.actualIterations + task2.actualIterations;

      long duration = currentTimeMillis() - start;
      LOGGER.debug("{} ms, {} iterations", duration, actualIterations);

      // why starting phase is unstable?
    }
  }
}
