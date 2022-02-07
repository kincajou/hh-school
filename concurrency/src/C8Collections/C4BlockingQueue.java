package C8Collections;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

// Another example of working with wallets - this time we use BlockingQueue and 3 threads.
// One thread executes the tasks in queue, other threads are generating those tasks.

public class C4BlockingQueue {

  static class Wallet {
    int money;
  }

  record TransferTask(Wallet fromWallet, Wallet toWallet, int money) {
    void perform() {
      fromWallet.money -= money;
      toWallet.money += money;
    }
  }

  private static final BlockingQueue<TransferTask> TASKS = new ArrayBlockingQueue<>(1000);

  // add new task to queue
  private static Runnable createTransferTask(Wallet fromWallet, Wallet toWallet, int money) {
    return () -> {
      try {
        int i = 0;
        while (true) {
          // this one blocks until there is enough space in queue
          // another methods allow putting with timeout, or put immediately if there is enough space without blocking
          TASKS.put(new TransferTask(fromWallet, toWallet, money));
          i++;
          if (i % 100 == 0) {
            System.out.printf("%s made %d transfers, %d tasks pending%n", Thread.currentThread().getName(), i, TASKS.size());
          }
        }
      }
      catch (InterruptedException e) {
        System.out.println("Interrupted");
      }
    };
  }

  // take task from queue and perform it
  private static Runnable createProcessingTask() {
    return () -> {
      try {
        while (true) {
          // this method also blocks, waiting until there is available element in queue
          // other methods allow getting element with timeout or getting it if it is available without blockin
          TASKS.take().perform();
        }
      }
      catch (InterruptedException e) {
        System.out.println("Interrupted");
      }
    };
  }

  public static void main(String[] args) {

    Wallet wallet1 = new Wallet();
    Wallet wallet2 = new Wallet();

    new Thread(createProcessingTask(), "Processing").start();

    Runnable wallet1to2Task = createTransferTask(wallet1, wallet2, 1);
    Runnable wallet2to1Task = createTransferTask(wallet2, wallet1, 1);

    new Thread(wallet1to2Task, "1 to 2").start();
    new Thread(wallet2to1Task, "2 to 1").start();

  }
}
