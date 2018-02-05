package C7DeadLocks;

import java.util.Arrays;
import java.util.Comparator;

public class D2Order {

  // Let's always synchronize in particular order.

  static class Wallet {
    final int walletId;

    Wallet(int walletId) {
      this.walletId = walletId;
    }

    int money;
  }

  static void transfer(Wallet fromWallet, Wallet toWallet, int money) {
    Wallet[] wallets = new Wallet[] {fromWallet, toWallet};
    Arrays.sort(wallets, Comparator.comparingInt(w -> w.walletId));
    synchronized (wallets[0]) {
      synchronized (wallets[1]) {
        fromWallet.money -= money;
        toWallet.money += money;
      }
    }
  }

  static Runnable createTransferTask(Wallet fromWallet, Wallet toWallet, int money) {
    return () -> {
      int i = 0;
      while (true) {
        transfer(fromWallet, toWallet, money);
        i++;
        if (i % 100 == 0) {
          System.out.println(Thread.currentThread().getName() + " made " + i + " transfers");
        }
      }
    };
  }

  public static void main(String[] args) {

    Wallet wallet1 = new Wallet(1);
    Wallet wallet2 = new Wallet(2);

    Runnable wallet1to2Task = createTransferTask(wallet1, wallet2, 1);
    Runnable wallet2to1Task = createTransferTask(wallet2, wallet1, 1);

    new Thread(wallet1to2Task, "1 to 2").start();
    new Thread(wallet2to1Task, "2 to 1").start();

  }
}
