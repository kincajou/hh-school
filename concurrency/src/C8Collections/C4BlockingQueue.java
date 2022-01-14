package C8Collections;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

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

  private static Runnable createProcessingTask() {
    return () -> {
      try {
        while (true) {
          TASKS.take().perform();
        }
      }
      catch (InterruptedException e) {
        System.out.println("Interrupted");
      }
    };
  }

  private static Runnable createTransferTask(Wallet fromWallet, Wallet toWallet, int money) {
    return () -> {
      try {
        int i = 0;
        while (true) {
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
