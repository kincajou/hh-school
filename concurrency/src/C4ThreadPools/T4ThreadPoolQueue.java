package C4ThreadPools;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.lang.System.currentTimeMillis;

public class T4ThreadPoolQueue {

  // It is often a good idea to limit thread pool queue size.
  // What to do with rejected task dependents on the nature of the task.
  //
  // For real time site requests we use:
  // - large thread pools (100-1000 threads)
  // - zero length queue (many threads is a sort of a queue itself, it is bad to let requests sour in a long queue)
  // - "fail fast on reject" behaviour
  //
  // For offline tasks we use:
  // - small thread pools (<100 threads)
  // - moderate size queue (100-10_000)
  // - "block on reject" behaviour

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

  public static void main(String[] args) throws InterruptedException, ExecutionException {

    int maxQueueSize = 100;
    BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(maxQueueSize);
    Executor executor =
        new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, blockingQueue);

    int requests = 0;
    int rejects = 0;
    long start = currentTimeMillis();
    while (true) {

      try {
        executor.execute(new SmallTask());
        requests++;
      } catch (RejectedExecutionException e) {
        rejects++;
      }

      if (currentTimeMillis() - start > 1_000) {
        System.out.println(requests + " requests / sec, " + rejects + " rejects / sec");
        requests = 0;
        rejects = 0;
        start = currentTimeMillis();
      }
    }

  }
}
