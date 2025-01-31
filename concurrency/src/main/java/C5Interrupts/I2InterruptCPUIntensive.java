package C5Interrupts;

import java.util.concurrent.ThreadLocalRandom;

public class I2InterruptCPUIntensive {

  // We should periodically check interrupted flag.

  static class LongTask implements Runnable {

    int blackHole = 0;

    @Override
    public void run() {
      ThreadLocalRandom random = ThreadLocalRandom.current();
      while (!Thread.currentThread().isInterrupted()) {
        blackHole += random.nextInt();
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

  }
}
