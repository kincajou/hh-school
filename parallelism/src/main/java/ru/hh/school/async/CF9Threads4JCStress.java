package ru.hh.school.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.I_Result;

@JCStressTest
@Outcome(id = "1", expect = Expect.ACCEPTABLE, desc = "Future completed by Thread 3")
@Outcome(id = "2", expect = Expect.ACCEPTABLE, desc = "Future completed by Thread 55")
@Outcome(id = "3", expect = Expect.FORBIDDEN, desc = "Future completed by some other thread")
@State
public class CF9Threads4JCStress {
  private static final String THREAD_2_NAME = "Thread 2";
  private static final String THREAD_3_NAME = "Thread 3";
  private static final String THREAD_55_NAME = "Thread 55";

  private final ExecutorService executor = Executors.newSingleThreadExecutor(r -> new Thread(r, THREAD_3_NAME));

  @Actor
  public void actor(I_Result r) {
    r.r1 = get();
  }

  private int get() {
    CompletableFuture<String> promise = new CompletableFuture<>();
    CompletableFuture<String> promise2 = new CompletableFuture<>();

    CompletableFuture<String> promiseWithModifiers = promise
        .thenApply(s -> s + ", Петя")
        .thenApplyAsync(s -> s + ", Аня", executor)
        .thenApply(s -> s + ", Света")
        .thenCombine(promise2, (s1, s2) -> s1 + " а так же " + s2)
        .thenApply(s -> Thread.currentThread().getName());

    new Thread(() -> promise.complete("Витя"), THREAD_2_NAME).start();
    new Thread(() -> promise2.complete("Сережа"), THREAD_55_NAME).start();

    var combineThreadName = promiseWithModifiers.join();
    if (combineThreadName.equals(THREAD_3_NAME)) {
      return 1;
    }
    if (combineThreadName.equals(THREAD_55_NAME)) {
      return 2;
    }
    return 3;
  }
}
