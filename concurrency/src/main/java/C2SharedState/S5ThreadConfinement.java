package C2SharedState;

import static java.lang.System.currentTimeMillis;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class S5ThreadConfinement {

  // Instead of using fancy tools, just do not share state.
  // The code is simpler.

  private static final Logger LOGGER = getLogger(S5ThreadConfinement.class);

  // different implementation of task
  static class ThreadConfinementTask implements Runnable {

    final int iterations;

    ThreadConfinementTask(int iterations) {
      this.iterations = iterations;
    }

    // just a local field
    int actualIterations = 0;

    @Override
    public void run() {
      for (int i = 0; i < iterations; i++) {
        actualIterations++;
      }
    }
  }

  public static void main(String[] args) throws InterruptedException {

    int iterations = 100_000_000;
    int numOfThreads = 2;

    while (true) {
      long start = currentTimeMillis();

      // no shared state here
      ThreadConfinementTask task1 = new ThreadConfinementTask(iterations / numOfThreads);
      ThreadConfinementTask task2 = new ThreadConfinementTask(iterations / numOfThreads);
      Thread thread1 = new Thread(task1, "Task thread 1");
      Thread thread2 = new Thread(task2, "Task thread 2");
      thread1.start();
      thread2.start();
      thread1.join();
      thread2.join();
      // get the total result
      int actualIterations = task1.actualIterations + task2.actualIterations;

      long duration = currentTimeMillis() - start;
      LOGGER.debug("{} ms, {} iterations", duration, actualIterations);

      // will it work faster or slower?
    }
  }
}
