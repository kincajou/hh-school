package ru.hh.school.async;

import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class CF3ThenCompose {

  private static final Logger LOGGER = getLogger(CF3ThenCompose.class);

  public static void main(String[] args) {

    getData()
      .thenCompose(data -> getLength(data))
      .thenAccept(length -> LOGGER.debug("Length: {}", length))
      .join();

  }

  public static CompletableFuture<String> getData() {
    return CompletableFuture.completedFuture("Pizza");
  }

  public static CompletableFuture<Integer> getLength(String data) {
    CompletableFuture<String> promise = new CompletableFuture<>();
    CompletableFuture<Integer> result = promise.thenApply(string -> string.length());
    promise.complete(data);
    return result;
  }

}
