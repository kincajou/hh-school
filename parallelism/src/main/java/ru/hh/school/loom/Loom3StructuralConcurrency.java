package ru.hh.school.loom;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

// Structural concurrency is still a preview feature, needs --enable-preview jvm argument and api is subject to change
public class Loom3StructuralConcurrency {

  private static final Logger LOGGER = getLogger(Loom3StructuralConcurrency.class);

  private static final Random RANDOM = new Random();

  public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
    LOGGER.debug("StructuredTaskScope.ShutdownOnSuccess");
    // captures first success result, interrupts other unfinished threads
    try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {
      scope.fork(() -> get("Foo"));
      scope.fork(() -> {
        return get("Bar");
//        throw new RuntimeException("zxc");
      });
      scope.join();
      LOGGER.debug("Result is {}", scope.result());
    }


    LOGGER.debug("StructuredTaskScope.ShutdownOnFailure");
    // throws on first failure, interrupts other unfinished threads
    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
      StructuredTaskScope.Subtask<String> foo = scope.fork(() -> get("Foo"));
      StructuredTaskScope.Subtask<String> bar = scope.fork(() -> {
         return get("Bar");
//        throw new RuntimeException("zxc");
      });
      scope.join();
//      scope.joinUntil(Instant.now().plusMillis(10000));
//      scope.throwIfFailed();
      LOGGER.debug("Foo is {}", foo.state());
      LOGGER.debug("Bar is {}", bar.state());
    }
  }

  private static String get(String string) {
    int random = RANDOM.nextInt(10);
    LOGGER.debug("{}, delay: {}", string, random);
    try {
      Thread.sleep(random);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      LOGGER.error("Interrupted: {}", string);
    }
    return string;
  }
}
