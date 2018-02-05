package C5Interrupts;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

public class I1InterruptThread {

  // What if we are running some task, but it is time to stop the server.
  // For example for a new release.
  // Will this code stop?

  static class LongTask implements Runnable {

    int blackHole = 0;

    @Override
    public void run() {
      ThreadLocalRandom random = ThreadLocalRandom.current();
      while (true) {
        blackHole += random.nextInt();
      }
    }
  }

  public static void main(String[] args) throws InterruptedException, ExecutionException {

    LongTask longTask = new LongTask();

    Thread thread = new Thread(longTask, "long task");
    thread.start();

    Thread.sleep(10L);  // wait thread to work a bit

    thread.interrupt();
    thread.join();

    System.out.println("Thread stopped, " + longTask.blackHole);

  }
}
