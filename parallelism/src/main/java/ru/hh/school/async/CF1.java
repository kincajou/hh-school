package ru.hh.school.async;

import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.slf4j.LoggerFactory.getLogger;

public class CF1 {

  private static final Logger LOGGER = getLogger(CF1.class);

  public static void main(String[] args) {
    // print the output
    CompletableFuture.supplyAsync(() -> "1. Completed")
      .thenAccept(LOGGER::debug);


    // modify the output
    CompletableFuture.supplyAsync(() -> "2. Completed")
      .thenApply(string -> string + " this future")
      .thenAccept(LOGGER::debug);


    // continue the chain
    CompletableFuture.supplyAsync(() -> "3. Completed")
      .thenApply(string -> string + " this future")
      .thenAccept(LOGGER::debug)
      .thenRun(() -> LOGGER.debug("but can keep going"))
      .thenRun(() -> LOGGER.debug("on and on and on"))
      .thenApply(v -> 42)
      .thenAccept(n -> LOGGER.debug("and the answer is {}", n));

    sleepUninterruptibly(1, SECONDS);
  }

}
