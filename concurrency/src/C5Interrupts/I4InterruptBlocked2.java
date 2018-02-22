package C5Interrupts;

import java.util.concurrent.ThreadLocalRandom;

public class I4InterruptBlocked2 {

  // We should somehow react to InterruptedException.
  // It is often not easy. Consider complicated code with many deep methods throwing InterruptedException.

  static class LongTask implements Runnable {

    int blackHole = 0;

    @Override
    public void run() {
      ThreadLocalRandom random = ThreadLocalRandom.current();
      while (!Thread.currentThread().isInterrupted()) {
        try {
          blackHole += random.nextInt();
          deepMethod();
        } catch (RuntimeException e) {
          System.out.println("failed to save vacancy: " + e);
        }
      }
    }

    private static void deepMethod() {
      try {
        Thread.sleep(1000L);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException("interrupted while waiting DB", e);
      }
    }
  }

  public static void main(String[] args) throws InterruptedException {

    LongTask longTask = new LongTask();

    Thread thread = new Thread(longTask, "long task");
    thread.start();

    Thread.sleep(10L);

    thread.interrupt();
    thread.join();

    System.out.println("Thread stopped, " + longTask.blackHole);

  }
}
