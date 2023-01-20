package C3Visibility;

import common.Task;
import static java.lang.System.currentTimeMillis;

public class V1NoVisibility {

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

  public static void main(String[] args) {

    int iterations = 15_000;

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
    System.out.println(duration + " ms, blackhole: " + task.getBlackHole());
  }
}
