package ru.hh.school.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class CF8Threads3 {

  private static final Logger LOGGER = getLogger(CF8Threads3.class);

  public static void main(String[] args) throws InterruptedException {
    ExecutorService executor = Executors.newSingleThreadExecutor(r -> new Thread(r, "Thread 3"));

    CompletableFuture<String> promise = new CompletableFuture<>();
    promise
      .thenApply(s -> s + ", Петя")
      .thenApplyAsync(s -> s + ", Аня", executor)
      .thenApply(s -> s + ", Света")
      .thenApply(s -> s + " едят пиццу")
      .thenAccept(LOGGER::debug);

    new Thread(() -> promise.complete("Витя"), "Thread 2").start();

    promise.join();

    executor.awaitTermination(1, TimeUnit.SECONDS);
    executor.shutdown();
  }

}
