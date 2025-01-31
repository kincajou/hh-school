package common;

import org.openjdk.jmh.infra.Blackhole;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class Task implements Runnable {

  private static final Logger LOGGER = getLogger(Task.class);

  private final int iterations;
  private final boolean logFinished;

  public Task(int iterations) {
    this(iterations, true);
  }

  public Task(int iterations, boolean logFinished) {
    this.iterations = iterations;
    this.logFinished = logFinished;
  }

  @Override
  public void run() {
    for (int i = 0; i < iterations; i++) {
      // can we remove blackHole?
      Blackhole.consumeCPU(10);
      onIteration();
    }
    if (logFinished) {
      LOGGER.debug("Finished iterating");
    }
  }

  protected void onIteration() {
  }
}
