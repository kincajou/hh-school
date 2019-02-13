package C7DeadLocks;

public class D1DeadLock {

  // Does this program always make progress?
  // Let's check with jstack

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
