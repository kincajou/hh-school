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

  public static void main(String[] args) throws InterruptedException {

    int iterations = 10_000;

    long start = currentTimeMillis();

    NoVisibilityTask task = new NoVisibilityTask(iterations);
    Thread thread = new Thread(task, "thread1");
    thread.start();

    while (true) {
      if (task.actualIterations == iterations) {
        break;
      }
    }

    long duration = currentTimeMillis() - start;
    System.out.println(duration + " ms, " + task.getBlackHole());

  }
}
