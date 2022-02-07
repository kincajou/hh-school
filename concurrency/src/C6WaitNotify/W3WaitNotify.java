package C6WaitNotify;

public class W3WaitNotify {

  // More optimized version, that does not wake up all waiters.

  static class SingularQueue<T> implements Producer<T>, Consumer<T> {

    private final Object noTaskMonitor = new Object();
    private final Object taskReadyMonitor = new Object();

    private T task;

    @Override
    public void produce(T task) throws InterruptedException {
      synchronized (noTaskMonitor) {
        if (this.task != null) {
          noTaskMonitor.wait();
        }
        synchronized (taskReadyMonitor) {
          this.task = task;
          taskReadyMonitor.notify();
        }
      }
    }

    @Override
    public T consume() throws InterruptedException {
      synchronized (taskReadyMonitor) {
        if (task == null) {
          taskReadyMonitor.wait();
        }
        synchronized (noTaskMonitor) {
          T task = this.task;
          this.task = null;
          noTaskMonitor.notify();
          return task;
        }
      }
    }
  }

  public static void main(String[] args) throws InterruptedException {

    SingularQueue<String> singularQueue = new SingularQueue<>();

    Runnable consumerTask = () -> {
      while(!Thread.currentThread().isInterrupted()) {
        String string;
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

    Thread.sleep(1000L);

    consumerThread.interrupt();
    consumerThread.join();

    // this is more optimized example, but it looks very complicated because of all those synchronized blocks, and it is easy to make a mistakes in such code.
  }
}
