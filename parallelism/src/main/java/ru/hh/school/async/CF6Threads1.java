package ru.hh.school.async;

import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class CF6Threads1 {

  private static final Logger LOGGER = getLogger(CF6Threads1.class);

  public static void main(String[] args) {
    CompletableFuture<String> promise = new CompletableFuture<>();
    CompletableFuture<Void> promiseWithModifiers = promise
        .thenApply(s -> s + ", Петя")
        .thenApply(s -> s + ", Аня")
        .thenApply(s -> s + ", Света")
        .thenApply(s -> s + " едят пиццу")
        .thenAccept(LOGGER::debug);

    promise.complete("Витя");

    promiseWithModifiers.join();
  }

}
