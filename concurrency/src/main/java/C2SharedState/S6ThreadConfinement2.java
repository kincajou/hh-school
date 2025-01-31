package C2SharedState;

import common.Task;
import static java.lang.System.currentTimeMillis;

public class S6ThreadConfinement2 {

  // Uses Task that prevents loop unrolling.
  // -XX:+PrintCompilation
  // https://stackoverflow.com/a/41154126

  static class ThreadConfinementTask extends Task {

    ThreadConfinementTask(int iterations) {
      super(iterations);
    }

    int actualIterations;

    @Override
    protected void onIteration() {
      actualIterations++;
    }
  }

  public static void main(String[] args) throws InterruptedException {

    int iterations = 1_000_000_000;
    int numOfThreads = 2;

    while (true) {
      long start = currentTimeMillis();

      ThreadConfinementTask task1 = new ThreadConfinementTask(iterations / numOfThreads);
      ThreadConfinementTask task2 = new ThreadConfinementTask(iterations / numOfThreads);
      Thread thread1 = new Thread(task1, "Task thread 1");
      Thread thread2 = new Thread(task2, "Task thread 2");
      thread1.start();
      thread2.start();
      thread1.join();
      thread2.join();
      int actualIterations = task1.actualIterations + task2.actualIterations;
      int blackHole = task1.getBlackHole() + task2.getBlackHole();

      long duration = currentTimeMillis() - start;
      System.out.println(duration + " ms, " + actualIterations + " iterations, blackhole: " + blackHole);

      // why starting phase is unstable?
    }
  }
}
