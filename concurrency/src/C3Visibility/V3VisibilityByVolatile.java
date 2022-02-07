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

  // - this example with volatile is much faster than previous example with synchronized.
  // Access to volatile variable is always performed in main memory, bypassing all CPU caches and that allows it to always have actual value.
  // This is much slower and since java 1.5 volatile variable also works as barrier - making cache coherency for all other thread variables.
  // Barrier also dictates that runtime compilation can't change order of operations before and after working with volatile.
  //
  // But volatile is not atomic - ++ operation has to first read and then write incremented value. There can be another thread that
  // changes the value between those two operations, making it inconsistent.
}
