package C5Interrupts;

import java.util.concurrent.ThreadLocalRandom;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class I4InterruptBlocked2 {

  // We should somehow react to InterruptedException.
  // It is often not easy. Consider complicated code with many deep methods throwing InterruptedException.
  // https://www.yegor256.com/2015/10/20/interrupted-exception.html

  private static final Logger LOGGER = getLogger(I4InterruptBlocked2.class);

  static class LongTask implements Runnable {

    int blackHole = 0;

    @Override
    public void run() {
      ThreadLocalRandom random = ThreadLocalRandom.current();
      while (!Thread.currentThread().isInterrupted()) {
        try {
          blackHole += random.nextInt();
          deepMethod();
        } catch (RuntimeException e) {
          LOGGER.error("failed to process deep method", e);
        }
      }
    }

    private static void deepMethod() { // catch InterruptedException or propagate it up?
      try {
        Thread.sleep(1000L);
      } catch (InterruptedException e) {
        // don't swallow InterruptedException
        Thread.currentThread().interrupt();
        throw new RuntimeException("interrupted while sleeping", e);
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
  }
}
