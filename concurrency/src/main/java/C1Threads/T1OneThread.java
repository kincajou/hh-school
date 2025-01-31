package C1Threads;

import common.Task;
import static java.lang.System.currentTimeMillis;

public class T1OneThread {

  // One (main) thread does some work.
  // In this case - adds random numbers.
  // Not useful enough. But pretend - it is)
  // Uses only one CPU core.
  // How can we use more cores?

  public static void main(String[] args) {

    int iterations = 1_000_000_000;

    while (true) {
      long start = currentTimeMillis();

      Task task = new Task(iterations);
      task.run();

      long duration = currentTimeMillis() - start;
      System.out.println(duration + " ms, blackhole: " + task.getBlackHole());
    }

  }
}
