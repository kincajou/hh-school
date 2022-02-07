package C2SharedState;

import common.Task;

public class S1SharedState {

  // What if we want to share some state between threads?
  // For example, number of some page views.

  static class SharedStateTask extends Task {

    SharedStateTask(int iterations) {
      super(iterations);
    }

    // shared state
    int actualIterations = 0;

    @Override
    protected void onIteration() {
      actualIterations++;
    }
  }

  public static void main(String[] args) throws InterruptedException {

    int iterations = 1_000_000_000;
    int numOfThreads = 2;

    SharedStateTask task = new SharedStateTask(iterations / numOfThreads);
    Thread thread1 = new Thread(task, "Task thread 1");
    Thread thread2 = new Thread(task, "Task thread 2");
    thread1.start();
    thread2.start();
    thread1.join();
    thread2.join();
    System.out.println("Expected " + iterations + ", got " + task.actualIterations + ", blackhole: " + task.getBlackHole());

    // how much do we get in output?

    // - actualIterations field is a shared state between two different threads and is not protected against concurrent writes
    // we will get about half of expected iterations because actualIterations field is overwritten continuously in unsafe manner
    // each thread has its own copy of that variable in L1/L2/L3 CPU cache, and exactly that copy is incremented
    // we have no barriers (places in code that do cache synchronization with main memory) here except non-obvious one in System.out.println and thread::join
  }
}
