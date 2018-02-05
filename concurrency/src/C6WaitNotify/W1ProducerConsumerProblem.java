package C6WaitNotify;

public class W1ProducerConsumerProblem {

  // We saw BlockingQueue in front of a thread pool.
  // Blocking queue is a common tool to decouple producers from consumers.
  // Consider a simplified implementation.
  // Any problem?

  static class SingularQueue<T> implements Producer<T>, Consumer<T> {

    private volatile T task;

    @Override
    public void produce(T task) {
      while(this.task != null) {}
      this.task = task;
      while(task.equals(this.task)) {}
    }

    @Override
    public T consume() {
      while (this.task == null && !Thread.currentThread().isInterrupted()) {}
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

    Thread.sleep(10L);  // sleep is always suboptimal, indicates lack of synchronization

    consumerThread.interrupt();
    consumerThread.join();

  }
}
