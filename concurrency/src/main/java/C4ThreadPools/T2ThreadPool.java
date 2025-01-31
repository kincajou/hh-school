package C4ThreadPools;

import static java.lang.System.currentTimeMillis;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class T2ThreadPool {

  // Let's reuse threads.

  private static final Logger LOGGER = getLogger(T2ThreadPool.class);

  static class SmallTask implements Callable<Integer> {
    @Override
    public Integer call() {
      return ThreadLocalRandom.current().nextInt();
    }
  }

  public static void main(String[] args) throws InterruptedException, ExecutionException {

    int iterations = 100_000;

    // again, no concurrency here (single thread in executor) - it is intended!

    while (true) {
      long start = currentTimeMillis();

      ExecutorService executorService = Executors.newSingleThreadExecutor();
      // See many other factory methods in the Executors class.

      int blackHole = 0;
      for (int i = 0; i < iterations; i++) {
        SmallTask task = new SmallTask();
        Future<Integer> future = executorService.submit(task);
        blackHole += future.get();
      }

      // don't forget to shut down the executor

      // stops accepting new tasks
      executorService.shutdown();
      // waits until all running tasks finish or timeout happens
      boolean finished = executorService.awaitTermination(10L, TimeUnit.MILLISECONDS);

      if (!finished) {
        // interrupts all running threads, still no guarantee that everything finished
        executorService.shutdownNow();
      }

      long duration = currentTimeMillis() - start;
      float taskDuration = (float) duration / iterations;
      LOGGER.debug("{} ms / {} tasks = {} ms / task, blackhole: {}", duration, iterations, taskDuration, blackHole);
    }

  }
}
