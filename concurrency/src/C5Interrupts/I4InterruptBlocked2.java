package C5Interrupts;

import java.util.concurrent.ThreadLocalRandom;

public class I4InterruptBlocked2 {

  // We should somehow react to InterruptedException.
  // It is often not easy. Consider complicated code with many deep methods throwing InterruptedException.
  // https://www.yegor256.com/2015/10/20/interrupted-exception.html

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
          System.out.println("failed process deep method: " + e);
        }
      }
    }

    private static void deepMethod() { // catch InterruptedException or propagate it up?
      try {
        Thread.sleep(1000L);
      } catch (InterruptedException e) {
        // don't swallow InterruptedException
        Thread.currentThread().interrupt();
        throw new RuntimeException("interrupted while sleeping", e);
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

    // this example works correctly. See the article link above for examples of how to properly handle InterruptedException in different situations.
    // Basically if you can handle InterruptedException - catch it and reset the flag with "Thread.currentThread().interrupt()".
    // If you can't handle it, throw it.
    // Do not swallow the exception without resetting the flag.
    // There is Thread.interrupted() method that resets the flags - use with caution. Normally use Thread.currentThread().isInterrupted() - it
    // does not reset the flag.

  }
}
