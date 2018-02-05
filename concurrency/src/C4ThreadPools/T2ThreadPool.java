package C4ThreadPools;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static java.lang.System.currentTimeMillis;

public class T2ThreadPool {

  // Let's reuse threads.

  static class SmallTask implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
      return ThreadLocalRandom.current().nextInt();
    }
  }

  public static void main(String[] args) throws InterruptedException, ExecutionException {

    int iterations = 100_000;

    while (true) {
      long start = currentTimeMillis();

      ExecutorService executorService = Executors.newSingleThreadExecutor();
      // See many other factory methods in the Executors class.

      int blackHole = 0;
      for (int i=0; i<iterations; i++) {
        SmallTask task = new SmallTask();
        Future<Integer> future = executorService.submit(task);
        blackHole += future.get();
      }

      executorService.shutdown();
      executorService.awaitTermination(10L, TimeUnit.MILLISECONDS);

      long duration = currentTimeMillis() - start;
      float taskDuration = (float) duration / iterations;
      System.out.println(duration + " ms / " + iterations + " tasks = " + taskDuration + " ms / task, " + blackHole);
    }

  }
}
