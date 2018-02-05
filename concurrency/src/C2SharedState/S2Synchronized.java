package C2SharedState;

import common.Task;

import static java.lang.System.currentTimeMillis;

public class S2Synchronized {

  // Synchronized block prevents parallel execution of threads locking on the same monitor.

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
    int numOfThreads = 2;

    while (true) {
      long start = currentTimeMillis();

      SynchronizedTask task = new SynchronizedTask(iterations / numOfThreads);
      Thread thread1 = new Thread(task, "thread1");
      Thread thread2 = new Thread(task, "thread2");
      thread1.start();
      thread2.start();
      thread1.join();
      thread2.join();

      long duration = currentTimeMillis() - start;
      System.out.println(duration + " ms, " + task.actualIterations + " iterations, " + task.getBlackHole());

    }
  }
}
