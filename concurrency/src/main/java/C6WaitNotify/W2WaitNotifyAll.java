package C6WaitNotify;

import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class W2WaitNotifyAll {

  // Wait / notify(All) can be used only inside synchronized block.
  // Wait unblocks synchronized monitor and "sleeps" until other thread calls notification on same monitor while synchronized on it.
  // Notify wakes up one arbitrary waiter.
  // NotifyAll wakes up all waiters.
  // Is it ok to use notify in this example?

  private static final Logger LOGGER = getLogger(W2WaitNotifyAll.class);

  static class SingularQueue<T> implements Producer<T>, Consumer<T> {

    private T task;

    @Override
    public synchronized void produce(T task) throws InterruptedException {
      // why this cycle?
      while (this.task != null) {
        // can be called in synchronized block only
        wait();
      }
      this.task = task;
      // can we use notify() here?
      notifyAll();
    }

    @Override
    public synchronized T consume() throws InterruptedException {
      while(this.task == null) {
        wait();
      }
      T task = this.task;
      this.task = null;
      // can we use notify() here?
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
        LOGGER.debug("Consumed {}", string);
      }
    };
    Thread consumerThread = new Thread(consumerTask, "consumer");
    consumerThread.start();

    singularQueue.produce("Hello, ");
    singularQueue.produce("world!");

    Thread.sleep(1000L);

    consumerThread.interrupt();
    consumerThread.join();

  }
}
