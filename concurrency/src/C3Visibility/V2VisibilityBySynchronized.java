package C3Visibility;

import common.Task;

import static java.lang.System.currentTimeMillis;

public class V2VisibilityBySynchronized {

  // Synchronized guarantees visibility of ALL variables (incl. i.e. blackhole) between threads that synchronize on the same monitor.
  // But it prevents parallel execution and is not cheap.

  static class SynchronizedTask extends Task {

    SynchronizedTask(int iterations) {
      super(iterations);
    }

    final Object monitor = new Object();
    int actualIterations = 0;

    @Override
    protected void onIteration() {
      synchronized (monitor) {
        actualIterations++;
      }
    }
  }

  public static void main(String[] args) throws InterruptedException {

    int iterations = 1_000_000_000;

    while (true) {
      long start = currentTimeMillis();

      SynchronizedTask task = new SynchronizedTask(iterations);
      Thread thread = new Thread(task, "Task thread");
      thread.start();
      while (true) {
        synchronized (task.monitor) {
          if (task.actualIterations >= iterations) {
            break;
          }
        }
        Thread.sleep(10L);
      }

      long duration = currentTimeMillis() - start;
      System.out.println(duration + " ms, blackhole: " + task.getBlackHole());
    }
  }
}
