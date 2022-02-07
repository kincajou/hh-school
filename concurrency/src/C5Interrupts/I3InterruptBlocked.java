package C5Interrupts;

import java.util.concurrent.ThreadLocalRandom;

public class I3InterruptBlocked {

  // Ok, we check interrupted flag.
  // But what if the thread is blocked.
  // For example, waiting response from database.

  static class LongTask implements Runnable {

    int blackHole = 0;

    @Override
    public void run() {
      ThreadLocalRandom random = ThreadLocalRandom.current();
      while (!Thread.currentThread().isInterrupted()) {
        blackHole += random.nextInt();
        try {
          // this is blocking call (it throws InterruptedException)
          // another example of blocking call is reading from a socket or waiting for future to return result.
          Thread.sleep(1000L);
        } catch (InterruptedException e) {
          System.out.println(Thread.currentThread().getName() + " got " + e);
        }
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
    // will it stop?

    // - this example won't stop because when we catch InterruptedException, the interrupted flag on the thread is reset.

  }
}
