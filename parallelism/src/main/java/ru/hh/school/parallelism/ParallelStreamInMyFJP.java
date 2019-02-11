package ru.hh.school.parallelism;

import com.google.common.util.concurrent.Uninterruptibles;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.slf4j.LoggerFactory.getLogger;

public class ParallelStreamInMyFJP {

  private static final Logger LOGGER = getLogger(ParallelStreamInMyFJP.class);

  public static void main(String[] args) throws InterruptedException {
    ForkJoinPool forkJoinPool = new ForkJoinPool(1);

    forkJoinPool.submit(() -> IntStream.range(0, 5).parallel().forEach(ParallelStreamInMyFJP::performTask));

    forkJoinPool.shutdown();
    forkJoinPool.awaitTermination(20, SECONDS);
  }

  private static void performTask(int i) {
    LOGGER.debug("Performing task {}", i);
    Uninterruptibles.sleepUninterruptibly(1, SECONDS);
  }
}
