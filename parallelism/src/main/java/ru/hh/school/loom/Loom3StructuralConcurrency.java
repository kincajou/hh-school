package ru.hh.school.loom;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

// Structural concurrency is still a preview feature, needs --enable-preview jvm argument and api is subject to change
public class Loom3StructuralConcurrency {

  private static final Random RANDOM = new Random();

  public static void main(String[] args) throws InterruptedException, ExecutionException {
    System.out.println("StructuredTaskScope.ShutdownOnSuccess");
    // captures first success result, interrupts other unfinished threads
    try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {
      scope.fork(() -> get("Foo"));
      scope.fork(() -> {
        return get("Bar");
//        throw new RuntimeException("zxc");
      });
      scope.join();
      System.out.println("Result is " + scope.result());
    }


    System.out.println("StructuredTaskScope.ShutdownOnFailure");
    // throws on first failure, interrupts other unfinished threads
    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
      StructuredTaskScope.Subtask<String> foo = scope.fork(() -> get("Foo"));
      StructuredTaskScope.Subtask<String> bar = scope.fork(() -> {
         return get("Bar");
//        throw new RuntimeException("zxc");
      });
      scope.join();//.throwIfFailed();
      System.out.println("Foo is " + foo.state());
      System.out.println("Bar is " + bar.state());
    }
  }

  private static String get(String string) {
    int random = RANDOM.nextInt(10);
    System.out.println(string + ", delay: " + random);
    try {
      Thread.sleep(random);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      System.out.println("Interrupted: " + string);
    }
    return string;
  }
}
