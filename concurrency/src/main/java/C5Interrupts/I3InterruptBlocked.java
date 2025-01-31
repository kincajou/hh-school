package C5Interrupts;

import java.util.concurrent.ThreadLocalRandom;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class I3InterruptBlocked {

  // Ok, we check interrupted flag.
  // But what if the thread is blocked.
  // For example, waiting response from database.

  private static final Logger LOGGER = getLogger(I3InterruptBlocked.class);

  static class LongTask implements Runnable {

    int blackHole = 0;

    @Override
    public void run() {
      ThreadLocalRandom random = ThreadLocalRandom.current();
      while (!Thread.currentThread().isInterrupted()) {
        blackHole += random.nextInt();
        try {
          Thread.sleep(1000L);
        } catch (InterruptedException e) {
          LOGGER.error("Interruped", e);
        }
      }
    }
  }

  public static void main(String[] args) throws InterruptedException {

    LongTask longTask = new LongTask();

    Thread thread = new Thread(longTask, "long task");
    thread.start();

    Thread.sleep(10L);

    thread.interrupt();
    thread.join();

    LOGGER.debug("Thread stopped, {}", longTask.blackHole);
    // will it stop?

  }
}
