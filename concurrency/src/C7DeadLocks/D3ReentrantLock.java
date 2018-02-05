package C7DeadLocks;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class D3ReentrantLock {

  // What if synchronized blocks are in completely different methods and we do not even know where?
  // We can use Lock.tryLock(time)

  static class Wallet {
    private final Lock lock = new ReentrantLock();
    int money;

    <V> V locked(Callable<V> task) throws Exception {
      boolean locked = lock.tryLock(10, TimeUnit.MILLISECONDS);
      try {
        if (!locked) {
          throw new TimeoutException("timed out while waiting lock on " + this);
        }
        return task.call();
      } finally {
        if (locked) {
          lock.unlock();
        }
      }
    }
  }

  static void transfer(Wallet fromWallet, Wallet toWallet, int money) throws Exception {
    fromWallet.locked(() -> {
      toWallet.locked(() -> {
        fromWallet.money -= money;
        toWallet.money += money;
        return null;
      });
      return null;
    });
  }

  static Runnable createTransferTask(Wallet fromWallet, Wallet toWallet, int money) {
    return () -> {
      int i = 0;
      while (true) {
        try {
          transfer(fromWallet, toWallet, money);
        } catch (Exception e) {
          System.out.println(Thread.currentThread().getName() + " got " + e);
          continue;
        }
        i++;
        if (i % 100 == 0) {
          System.out.println(Thread.currentThread().getName() + " made " + i + " transfers");
        }
      }
    };
  }

  public static void main(String[] args) {

    Wallet wallet1 = new Wallet();
    Wallet wallet2 = new Wallet();

    Runnable wallet1to2Task = createTransferTask(wallet1, wallet2, 1);
    Runnable wallet2to1Task = createTransferTask(wallet2, wallet1, 1);

    new Thread(wallet1to2Task, "1 to 2").start();
    new Thread(wallet2to1Task, "2 to 1").start();

  }
}
