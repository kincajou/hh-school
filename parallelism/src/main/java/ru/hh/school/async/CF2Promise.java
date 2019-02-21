package ru.hh.school.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.util.concurrent.TimeUnit.SECONDS;

public class CF2Promise {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(CF2Promise.class);

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    CompletableFuture<String> promise = new CompletableFuture<>();

    new Thread(() -> {
      sleepUninterruptibly(5, SECONDS);
      promise.complete("Pizza is delivered!");
    }).start();

    LOGGER.debug("Ok, where is my pizza? Is it delivered?");
    LOGGER.debug(promise.getNow("nope")); // non-blocking call
    sleepUninterruptibly(2, SECONDS);
    LOGGER.debug("Still waiting!");
    LOGGER.debug(promise.get()); // blocking call
  }
}
