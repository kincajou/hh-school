package ru.hh.school.loom;

import com.google.common.util.concurrent.Uninterruptibles;
import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class Loom2Run {

  private static final Logger LOGGER = getLogger(Loom2Run.class);

  public static void main(String[] args) throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(100_000);

    IntStream.range(0, 100_000).forEach(i -> Thread.startVirtualThread(() -> {
      Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));
      LOGGER.debug("{}", i);
      latch.countDown();
    }));

    latch.await();
  }

  // Thread t = Thread.ofVirtual().start(() -> ...);
  // Thread t = Thread.ofVirtual().unstarted(() -> ...); // not started
  // ThreadFactory tf = Thread.ofVirtual().factory(); // thread factory

}
