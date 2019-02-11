package ru.hh.school.async;

import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class CF4ThenCombine {

  private static final Logger LOGGER = getLogger(CF4ThenCombine.class);

  public static void main(String[] args) {

    getData1()
      .thenCombine(getData2(), (data1, data2) -> data1.length() + data2.length())
      .thenAccept(length -> LOGGER.debug("Length: {}", length))
      .join();

  }

  public static CompletableFuture<String> getData1() {
    return CompletableFuture.completedFuture("Pizza");
  }

  public static CompletableFuture<String> getData2() {
    return CompletableFuture.completedFuture("Wine");
  }
}
