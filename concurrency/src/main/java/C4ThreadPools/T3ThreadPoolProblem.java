package C4ThreadPools;

import static java.lang.System.currentTimeMillis;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class T3ThreadPoolProblem {

  // What if we have more requests that we can serve?
  // For example, updates of resumes.
  // Run with -Xmx32M

  private static final Logger LOGGER = getLogger(T3ThreadPoolProblem.class);

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
        LOGGER.debug("{} / sec", requests);
        requests = 0;
        start = currentTimeMillis();
      }
    }
  }
}
