package ru.hh.school.async;

import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class CF5ErrorHandling {

  private static final Logger LOGGER = getLogger(CF5ErrorHandling.class);

  public static void main(String[] args) {

    CompletableFuture<String> promise = new CompletableFuture<>();

    promise
      .thenApply(CF5ErrorHandling::logging)
      .thenApply(CF5ErrorHandling::throwing)
      .thenApply(CF5ErrorHandling::changing) // skipped
      .thenApply(CF5ErrorHandling::logging) // skipped
      .exceptionally(CF5ErrorHandling::handle)
      .thenApply(CF5ErrorHandling::logging);

    promise.complete("data");

    promise.join();

//  data: *--*       *--*
//               \     /
//  error:       *--*

  }

  private static String logging(String data) {
    LOGGER.debug("Logging: {}", data);
    return data;
  }

  private static String throwing(String data) {
    LOGGER.debug("Throwing: {}", data);
    throw new RuntimeException("error!");
  }

  private static String changing(String data) {
    LOGGER.debug("Changing: {}", data);
    return data + " " + data;
  }

  private static String handle(Throwable t) {
    LOGGER.debug("Handle: {}", t.getMessage());
    return "data after handle";
  }
}
