package ru.hh.school.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import ru.hh.school.async.trueevil.DeepThought;
import static org.slf4j.LoggerFactory.getLogger;

public class CF10PanDimensionalCreators {

  private static final Logger LOGGER = getLogger(CF10PanDimensionalCreators.class);

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    LOGGER.debug("Let's ask Deep Thought the Ultimate Question of Life, the Universe, and Everything");

    DeepThought deepThought = new DeepThought();

    CompletableFuture<String> answer = deepThought.request("What do you get when you multiply six by nine");
    LOGGER.debug("The answer is: {}", answer.get());

    deepThought.shutdown();
  }
}
