package C3Visibility;

import common.Task;

import static java.lang.System.currentTimeMillis;

public class V3VisibilityByVolatile {

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

    int iterations = 1_000_000_000;

    while (true) {
      long start = currentTimeMillis();

      VolatileTask task = new VolatileTask(iterations);
      Thread thread = new Thread(task, "Task thread");
      thread.start();
      while (true) {
        if (task.actualIterations >= iterations) {
          break;
        }
        Thread.sleep(10L);
      }

      long duration = currentTimeMillis() - start;
      System.out.println(duration + " ms, blackhole: " + task.getBlackHole());
    }
  }
}
