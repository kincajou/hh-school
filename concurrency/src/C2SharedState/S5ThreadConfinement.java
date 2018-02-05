package C2SharedState;

import static java.lang.System.currentTimeMillis;

public class S5ThreadConfinement {

  // Instead of using fancy tools, just do not share state.
  // The code is simpler.
  // And JIT loves it.
  // TODO: JIT bug if local variable actualIterations is published to instance property after cycle

  static class ThreadConfinementTask implements Runnable {

    final int iterations;

    ThreadConfinementTask(int iterations) {
      this.iterations = iterations;
    }

    int actualIterations = 0;

    @Override
    public void run() {
      for (int i = 0; i < iterations; i++) {
        actualIterations++;
      }
    }
  }

  public static void main(String[] args) throws InterruptedException {

    int iterations = 1_000_000_000;
    int numOfThreads = 2;

    while (true) {
      long start = currentTimeMillis();

      ThreadConfinementTask task1 = new ThreadConfinementTask(iterations / numOfThreads);
      ThreadConfinementTask task2 = new ThreadConfinementTask(iterations / numOfThreads);
      Thread thread1 = new Thread(task1, "thread1");
      Thread thread2 = new Thread(task2, "thread2");
      thread1.start();
      thread2.start();
      thread1.join();
      thread2.join();
      int actualIterations = task1.actualIterations + task2.actualIterations;

      long duration = currentTimeMillis() - start;
      System.out.println(duration + " ms, " + actualIterations + " iterations");

    }
  }
}
