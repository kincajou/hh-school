package ru.hh.school.loom;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class Loom1Run {

  private static final Logger LOGGER = getLogger(Loom1Run.class);

  public static void main(String[] args) {
    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
      IntStream.range(0, 100_000).forEach(i -> executor.submit(() -> {
        Thread.sleep(Duration.ofSeconds(1));
        LOGGER.debug("{}", i);
        return i;
      }));
    }
  }


}
