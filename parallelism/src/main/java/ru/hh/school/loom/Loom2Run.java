package ru.hh.school.loom;

import com.google.common.util.concurrent.Uninterruptibles;
import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

public class Loom2Run {

  public static void main(String[] args) throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(100_000);

    IntStream.range(0, 100_000).forEach(i -> Thread.startVirtualThread(() -> {
      Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));
      System.out.println(i);
      latch.countDown();
    }));

    latch.await();
  }

  // Thread t = Thread.ofVirtual().start(() -> ...);
  // Thread t = Thread.ofVirtual().unstarted(() -> ...); // not started
  // ThreadFactory tf = Thread.ofVirtual().factory(); // thread factory

}
