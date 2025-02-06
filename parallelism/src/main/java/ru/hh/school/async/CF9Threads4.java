package ru.hh.school.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class CF9Threads4 {

  private static final Logger LOGGER = getLogger(CF9Threads4.class);

  public static void main(String[] args) throws InterruptedException {
    ExecutorService executor = Executors.newSingleThreadExecutor(r -> new Thread(r, "Thread 3"));

    CompletableFuture<String> promise = new CompletableFuture<>();
    CompletableFuture<String> promise2 = new CompletableFuture<>();

    CompletableFuture<Void> promiseWithModifiers = promise
        .thenApply(s -> s + ", Петя")
        .thenApplyAsync(s -> s + ", Аня", executor)
        .thenApply(s -> s + ", Света")
        .thenCombine(promise2, (s1, s2) -> s1 + " а так же " + s2)
        .thenApply(s -> s + " пьют вино")
        .thenAccept(LOGGER::debug);

    new Thread(() -> promise.complete("Витя"), "Thread 2").start();
    new Thread(() -> promise2.complete("Сережа"), "Thread 55").start();

    promiseWithModifiers.join();

    executor.awaitTermination(1, TimeUnit.SECONDS);
    executor.shutdown();
  }

}
