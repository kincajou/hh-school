package C7DeadLocks;

import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class D1DeadLock {

  // Does this program always make progress?

  private static final Logger LOGGER = getLogger(D1DeadLock.class);

  static class Wallet {
    int money;
  }

  private static void transfer(Wallet fromWallet, Wallet toWallet, int money) {
    synchronized (fromWallet) {
      synchronized (toWallet) {
        fromWallet.money -= money;
        toWallet.money += money;
      }
    }
  }

  private static Runnable createTransferTask(Wallet fromWallet, Wallet toWallet, int money) {
    return () -> {
      int i = 0;
      while(true) {
        transfer(fromWallet, toWallet, money);
        i++;
        if (i % 100 == 0) {
          LOGGER.debug("made {} transfers", i);
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
