package C6WaitNotify;

public class W2WaitNotifyAll {

  // Wait / notify(All) can be used only inside synchronized block.
  // Wait unblocks synchronized monitor and "sleeps" until notification.
  // Notify wakes up one arbitrary waiter.
  // NotifyAll wakes up all waiters.
  // Is it ok to use notify in this example?

  static class SingularQueue<T> implements Producer<T>, Consumer<T> {

    private T task;

    @Override
    public synchronized void produce(T task) throws InterruptedException {
      while (this.task != null) {
        wait();
      }
      this.task = task;
      notifyAll();
    }

    @Override
    public synchronized T consume() throws InterruptedException {
      while(this.task == null) {
        wait();
      }
      T task = this.task;
      this.task = null;
      notifyAll();
      return task;
    }
  }

  public static void main(String[] args) throws InterruptedException {

    SingularQueue<String> singularQueue = new SingularQueue<>();

    Runnable consumerTask = () -> {
      while(!Thread.currentThread().isInterrupted()) {
        String string = null;
        try {
          string = singularQueue.consume();
        } catch (InterruptedException e) {
          return;
        }
        System.out.println("Consumed " + string);
      }
    };
    Thread consumerThread = new Thread(consumerTask, "consumer");
    consumerThread.start();

    singularQueue.produce("Hello, ");
    singularQueue.produce("world!");

    Thread.sleep(10L);

    consumerThread.interrupt();
    consumerThread.join();

  }
}
