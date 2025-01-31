package C7DeadLocks;

import java.util.Arrays;
import java.util.Comparator;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class D2DeadLockFix {

  // Let's always synchronize in particular order.
  // What if wallet will be the same?

  private static final Logger LOGGER = getLogger(D2DeadLockFix.class);

  static class Wallet {
    final int walletId;

    Wallet(int walletId) {
      this.walletId = walletId;
    }

    int money;
  }

  private static void transfer(Wallet fromWallet, Wallet toWallet, int money) {
    Wallet[] wallets = new Wallet[] {fromWallet, toWallet};
    Arrays.sort(wallets, Comparator.comparingInt(w -> w.walletId));
    synchronized (wallets[0]) {
      synchronized (wallets[1]) {
        fromWallet.money -= money;
        toWallet.money += money;
      }
    }
  }

  private static Runnable createTransferTask(Wallet fromWallet, Wallet toWallet, int money) {
    return () -> {
      int i = 0;
      while (true) {
        transfer(fromWallet, toWallet, money);
        i++;
        if (i % 100 == 0) {
          LOGGER.debug("made {} transfers", i);
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
