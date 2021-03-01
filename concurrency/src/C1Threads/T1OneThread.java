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
      // In this and other examples we will print 'black hole' to console
      // in order to show that we need the result of the computation,
      // so JIT does not throw the computation away.
      System.out.println(duration + " ms, blackhole: " + task.getBlackHole());
    }

  }
}
