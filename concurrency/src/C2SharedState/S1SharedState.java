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
    Thread thread1 = new Thread(task, "thread1");
    Thread thread2 = new Thread(task, "thread2");
    thread1.start();
    thread2.start();
    thread1.join();
    thread2.join();
    System.out.println("Expected " + iterations + ", got " + task.actualIterations + ", " + task.getBlackHole());
    // how much do we get in output?

  }
}
