package C4ThreadPools;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static java.lang.System.currentTimeMillis;

public class T3ThreadPoolProblem {

  // What if we have more requests that we can serve?
  // For example, updates of resumes.
  // Run with -Xmx32M

  static class SmallTask implements Runnable {
    @Override
    public void run() {
      try {
        Thread.sleep(1L);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  public static void main(String[] args) {

    Executor executor = Executors.newSingleThreadExecutor();

    int requests = 0;
    long start = currentTimeMillis();
    while (true) {

      executor.execute(new SmallTask());

      requests++;
      if (currentTimeMillis() - start >= 1_000) {
        System.out.println(requests + " / sec");
        requests = 0;
        start = currentTimeMillis();
      }
    }
  }
}
