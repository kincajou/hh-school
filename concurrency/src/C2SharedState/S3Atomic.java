package C2SharedState;

import common.Task;

import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.currentTimeMillis;

public class S3Atomic {

  // AtomicInteger uses Compare and Swap (CAS)
  // Roughly:

  //  for (;;) {
  //    int current = get();
  //    int next = current + 1;
  //    if (hardware.compareAndSwap(current, next))
  //      return next;
  //  }
  //
  // Great on low contented threads, but bad on high.

  static class AtomicTask extends Task {

    AtomicTask(int iterations) {
      super(iterations);
    }

    // different types of Atomic exist
    final AtomicInteger actualIterations = new AtomicInteger();

    // same as synchronized block
    @Override
    protected void onIteration() {
      actualIterations.incrementAndGet();
    }
  }

  public static void main(String[] args) throws InterruptedException {

    int iterations = 1_000_000_000;
    int numOfThreads = 2;

    while (true) {
      long start = currentTimeMillis();

      AtomicTask task = new AtomicTask(iterations / numOfThreads);
      Thread thread1 = new Thread(task, "Task thread 1");
      Thread thread2 = new Thread(task, "Task thread 2");
      thread1.start();
      thread2.start();
      thread1.join();
      thread2.join();

      long duration = currentTimeMillis() - start;
      System.out.println(duration + " ms, " + task.actualIterations.get() + " iterations, blackhole: " + task.getBlackHole());

      // will this work better or worse than synchronized block?

      // - this will be faster than previous example, and is also simplier than synchronized blocks,
      // but we can do better with another concurrent value storage.
      // this example shows downside of AtomicInteger greatly since all it does is to create a heavy contention, and AtomicInteger
      // performs poorly in heavy contended environment (many writes, few reads).
    }
  }
}
