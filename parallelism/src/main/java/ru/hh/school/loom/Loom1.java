package ru.hh.school.loom;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Loom1 {

  public static void main(String[] args) {
    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
      IntStream.range(0, 100_000).forEach(i -> executor.submit(() -> {
        Thread.sleep(Duration.ofSeconds(1));
        System.out.println(i);
        return i;
      }));
    }
  }


}
