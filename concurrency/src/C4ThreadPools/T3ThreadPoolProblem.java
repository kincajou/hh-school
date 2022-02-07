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
        // won't happen in this example
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

  // - we generate tasks much faster than we can process them. Look into Executors.newSingleThreadExecutor() implementation -
  // it is just a wrapper around ThreadPoolExecutor constructor passing it unbounded LinkedBlockingQueue.
  // So eventually we will drop with OutOfMemoryError because there is not enough memory for more task instances.
}
