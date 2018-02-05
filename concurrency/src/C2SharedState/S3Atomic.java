package C2SharedState;

import common.Task;

import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.currentTimeMillis;

public class S3Atomic {

  // AtomicInteger uses Compare and Swap (CAS)
  // Roughly:
  //
  // do {
  //   currentVal = getCurrentVal();
  //   actualVal = hardware.compareAndSwap(currentVal, currentVal+1);
  // } while (actualVal != currentVal);
  //
  // Great on low contented threads, but bad on high.

  static class AtomicTask extends Task {

    AtomicTask(int iterations) {
      super(iterations);
    }

    final AtomicInteger actualIterations = new AtomicInteger();

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
      Thread thread1 = new Thread(task, "thread1");
      Thread thread2 = new Thread(task, "thread2");
      thread1.start();
      thread2.start();
      thread1.join();
      thread2.join();

      long duration = currentTimeMillis() - start;
      System.out.println(duration + " ms, " + task.actualIterations.get() + " iterations, " + task.getBlackHole());

    }
  }
}
