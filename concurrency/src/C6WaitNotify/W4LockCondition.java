package C6WaitNotify;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class W4LockCondition {

  // The same as previous version but using java.util.concurrent.locks.
  // No point of using Lock and Condition for this particular task.
  // But look at other methods provided by these interfaces, for example Lock.tryLock(time, unit).
  // Also look through other synchronizers: Semaphore, CountDownLatch, CyclicBarrier, etc.
  // http://docs.oracle.com/javase/8/docs/api/java/util/concurrent/package-summary.html

  static class SingularQueue<T> implements Producer<T>, Consumer<T> {

    private final Lock lock = new ReentrantLock();
    private final Condition noTaskCondition = lock.newCondition();
    private final Condition taskReadyCondition = lock.newCondition();

    private T task;

    @Override
    public void produce(T task) throws InterruptedException {
      lock.lock();
      try {
        if (this.task != null) {
          noTaskCondition.await();
        }
        this.task = task;
        taskReadyCondition.signal();
      } finally {
        lock.unlock();
      }
    }

    @Override
    public T consume() throws InterruptedException {
      lock.lock();
      try {
        if (task == null) {
          taskReadyCondition.await();
        }
        T task = this.task;
        this.task = null;
        noTaskCondition.signal();
        return task;
      } finally {
        lock.unlock();
      }
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
