package C2SharedState;

import common.Task;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class S1SharedState {

  // What if we want to share some state between threads?
  // For example, number of some page views.

  private static final Logger LOGGER = getLogger(S1SharedState.class);

  static class SharedStateTask extends Task {

    SharedStateTask(int iterations) {
      super(iterations);
    }

    // shared state
    int actualIterations = 0;

    @Override
    protected void onIteration() {
      actualIterations++;
    }
  }

  public static void main(String[] args) throws InterruptedException {

    int iterations = 100_000_000;
    int numOfThreads = 2;

    SharedStateTask task = new SharedStateTask(iterations / numOfThreads);
    Thread thread1 = new Thread(task, "Task thread 1");
    Thread thread2 = new Thread(task, "Task thread 2");
    thread1.start();
    thread2.start();
    thread1.join();
    thread2.join();
    LOGGER.debug("Expected {}, got {}", iterations, task.actualIterations);

    // how much do we get in output?
  }
}
