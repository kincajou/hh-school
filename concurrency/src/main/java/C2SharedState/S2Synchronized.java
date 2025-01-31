package C2SharedState;

import common.Task;
import static java.lang.System.currentTimeMillis;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class S2Synchronized {

  // Synchronized block prevents parallel execution of threads locking on the same monitor.

  private static final Logger LOGGER = getLogger(S2Synchronized.class);

  static class SynchronizedTask extends Task {

    SynchronizedTask(int iterations) {
      super(iterations);
    }

    // common object threads synchronize on
    // can't be primitive
    final Object monitor = new Object();

    // shared state
    int actualIterations = 0;

    @Override
    protected void onIteration() {
      synchronized (monitor) {
        actualIterations++;
      }
    }

    // synchronize on 'this' - worse than explicit monitor, can't know what else may sync this way, leading to problems
//    protected synchronized void onIteration() {
//      actualIterations++;
//    }
  }

  public static void main(String[] args) throws InterruptedException {

    int iterations = 100_000_000;
    int numOfThreads = 2;

    while (true) {
      long start = currentTimeMillis();

      SynchronizedTask task = new SynchronizedTask(iterations / numOfThreads);
      Thread thread1 = new Thread(task, "Task thread 1");
      Thread thread2 = new Thread(task, "Task thread 2");
      thread1.start();
      thread2.start();
      thread1.join();
      thread2.join();

      long duration = currentTimeMillis() - start;
      LOGGER.debug("{} ms, {} iterations", duration, task.actualIterations);

      // what's happening here:
      // - sync on monitor
      // - synchronized block resets cpu cache for 'actualIterations' to achieve coherency
    }
  }
}
