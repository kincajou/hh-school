package common;

import java.util.concurrent.ThreadLocalRandom;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class Task implements Runnable {

  private static final Logger LOGGER = getLogger(Task.class);

  private final int iterations;

  public Task(int iterations) {
    this.iterations = iterations;
  }

  private int blackHole;

  @Override
  public void run() {
    int blackHole = 0;
    ThreadLocalRandom random = ThreadLocalRandom.current();
    for (int i = 0; i < iterations; i++) {
      // can we remove blackHole increment?
      blackHole += random.nextInt();
      onIteration();
    }
    this.blackHole += blackHole;
    LOGGER.debug("Finished iterating");
  }

  protected void onIteration() {
  }

  public int getBlackHole() {
    return blackHole;
  }
}
