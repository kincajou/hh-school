package C3Visibility;

import common.Task;
import static java.lang.System.currentTimeMillis;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class V1NoVisibility {

  private static final Logger LOGGER = getLogger(V1NoVisibility.class);

  // Does it always stop?
  // Notice number of iterations.

  static class NoVisibilityTask extends Task {

    NoVisibilityTask(int iterations) {
      super(iterations);
    }

    int actualIterations = 0;

    @Override
    protected void onIteration() {
      actualIterations++;
    }
  }

  public static void main(String[] args) throws InterruptedException {

    int iterations = 100_000;

    long start = currentTimeMillis();

    NoVisibilityTask task = new NoVisibilityTask(iterations);
    Thread thread = new Thread(task, "Task thread");
    thread.start();

    while (true) {
      // can we put == here?
      if (task.actualIterations >= iterations) {
        break;
      }
      // will reading second time help?

      //if (task.actualIterations >= iterations) {
      //   break;
      // }

      // will sysout help?

      // System.out.println(task.actualIterations);

      // will another OS help?
    }

    long duration = currentTimeMillis() - start;
    LOGGER.debug("{} ms, value: {}", duration, task.actualIterations);
  }
}
