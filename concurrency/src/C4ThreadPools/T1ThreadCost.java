package C4ThreadPools;

import java.util.concurrent.ThreadLocalRandom;

import static java.lang.System.currentTimeMillis;

public class T1ThreadCost {

  // Ok, let's always use thread confined variables and publish results with Thread.join().
  // Will it work for small tasks?

  static class SmallTask implements Runnable {

    int blackHole;

    @Override
    public void run() {
      blackHole = ThreadLocalRandom.current().nextInt();
    }
  }

  public static void main(String[] args) throws InterruptedException {

    int iterations = 100_000;

    while (true) {
      long start = currentTimeMillis();

      int blackHole = 0;
      for (int i=0; i<iterations; i++) {
        SmallTask task = new SmallTask();
        Thread thread = new Thread(task, "Task thread");
        thread.start();
        thread.join();
        blackHole += task.blackHole;
      }

      long duration = currentTimeMillis() - start;
      float taskDuration = (float) duration / iterations;
      System.out.println(duration + " ms / " + iterations + " tasks = " + taskDuration + " ms / task, blackhole: " + blackHole);
    }
  }
}
