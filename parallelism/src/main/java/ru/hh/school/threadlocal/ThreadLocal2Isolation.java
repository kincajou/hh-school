package ru.hh.school.threadlocal;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class ThreadLocal2Isolation {

  private static final Logger LOGGER = getLogger(ThreadLocal2Isolation.class);

  private static final ThreadLocal<Integer> THREAD_LOCAL = new ThreadLocal<>();

  static void main() throws InterruptedException {

    CountDownLatch latch = new CountDownLatch(3);
    Random random = new Random();

    for (int i = 0; i < 3; i++) {
      new Thread(
          () -> {
            try {
              int value = random.nextInt(100);
              LOGGER.debug("Setting value to {}", value);
              THREAD_LOCAL.set(value);

              // simulate work
              TimeUnit.MILLISECONDS.sleep(random.nextInt(100));

              // ensure the value hasn't changed despite other threads running
              LOGGER.debug("Value is still {}", THREAD_LOCAL.get());

              // clean up is important
              THREAD_LOCAL.remove();
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
            } finally {
              latch.countDown();
            }
          }, "SampleThread-" + i
      ).start();
    }

    latch.await();
    LOGGER.debug("Main thread ThreadLocal: {}", THREAD_LOCAL.get());
  }
}
