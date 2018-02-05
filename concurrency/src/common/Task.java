package common;

import java.util.concurrent.ThreadLocalRandom;

public class Task implements Runnable {

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
      blackHole += random.nextInt();
      onIteration();
    }
    this.blackHole += blackHole;
  }

  protected void onIteration() {
  }

  public int getBlackHole() {
    return blackHole;
  }
}
