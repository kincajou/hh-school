package ru.hh.school.async;

import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class CF7Threads2 {

  private static final Logger LOGGER = getLogger(CF7Threads2.class);

  public static void main(String[] args) {
    CompletableFuture<String> promise = new CompletableFuture<>();
    promise
      .thenApply(s -> s + ", Петя")
      .thenApply(s -> s + ", Аня")
      .thenApply(s -> s + ", Света")
      .thenApply(s -> s + " едят пиццу")
      .thenAccept(LOGGER::debug);

    // sleep?
    new Thread(() -> promise.complete("Витя"), "Thread 2").start();

    promise.join();
  }

}
