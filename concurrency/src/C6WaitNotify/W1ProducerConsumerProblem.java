package C6WaitNotify;

public class W1ProducerConsumerProblem {

  // We used BlockingQueue backing a thread pool.
  // BlockingQueue is a common tool to decouple producers from consumers.
  // Consider a simplified implementation.
  // Any problems?

  static class SingularQueue<T> implements Producer<T>, Consumer<T> {

    private volatile T task; // does volatile helps here?

    @Override
    // will synchronized helps here?
    public void produce(T task) {
      while(this.task != null) {
        // do nothing
      }
      this.task = task;
      while(task.equals(this.task)) {
        // do nothing
      }
    }

    @Override
    // and/or here?
    public T consume() {
      while (this.task == null && !Thread.currentThread().isInterrupted()) {
        // do nothing
      }
      T task = this.task;
      this.task = null;
      return task;
    }
  }

  public static void main(String[] args) throws InterruptedException {

    SingularQueue<String> singularQueue = new SingularQueue<>();

    Runnable consumerTask = () -> {
      while(!Thread.currentThread().isInterrupted()) {
        String string = singularQueue.consume();
        System.out.println("Consumed " + string);
      }
    };
    Thread consumerThread = new Thread(consumerTask, "consumer");
    consumerThread.start();

    singularQueue.produce("Hello, ");
    singularQueue.produce("world!");

    Thread.sleep(100L);  // sleep is always suboptimal, indicates lack of synchronization

    consumerThread.interrupt();
    consumerThread.join();

    // what are the problems here?

  }
}
