package ru.hh.school.async.trueevil;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import org.slf4j.Logger;
import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.slf4j.LoggerFactory.getLogger;

public class DeepThought {

  private static final Logger LOGGER = getLogger(DeepThought.class);

  private static final Map<String, String> ANSWERS = Map.of("What do you get when you multiply six by nine", "42");

  private final ExecutorService mind = newSingleThreadExecutor(runnable -> new Thread(runnable, "Deep Thought Routine"));

  public CompletableFuture<String> request(String question) {
    CompletableFuture<String> promise = new CompletableFuture<>();
    mind.submit(new ThoughtRoutine(promise, question));
     promise.thenApply(TrueEvil::delay);
    return promise;
  }

  public void shutdown() {
    mind.shutdown();
  }

  public static class ThoughtRoutine implements Runnable {

    private final CompletableFuture<String> promise;
    private final String question;

    ThoughtRoutine(CompletableFuture<String> promise, String question) {
      this.promise = promise;
      this.question = question;
    }

    public void run() {
      LOGGER.debug("Gonna do some calculations");
      sleepUninterruptibly(1, SECONDS);
      promise.complete(ANSWERS.getOrDefault(question, "I don't know"));
      LOGGER.debug("Now i'm free to continue doing nothing");
    }
  }

}
