package C2SharedState;

import common.Task;
import static java.lang.System.currentTimeMillis;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class S7VarHandle {

  // VarHandle allows to select memory guarantees on read and write operations

  static class VarHandleTask extends Task {

    static int ACTUAL_ITERATIONS = 0;
    final static VarHandle HANDLE;

    static {
      try {
        HANDLE = MethodHandles
            .lookup()
            .findStaticVarHandle(VarHandleTask.class, "ACTUAL_ITERATIONS", int.class);
      } catch (NoSuchFieldException | IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }

    VarHandleTask(int iterations) {
      super(iterations);
    }

    @Override
    protected void onIteration() {
      HANDLE.getAndAdd(1);
    }
  }

  public static void main(String[] args) throws InterruptedException {

    int iterations = 1_000_000_000;
    int numOfThreads = 2;

    while (true) {
      long start = currentTimeMillis();

      VarHandleTask task = new VarHandleTask(iterations / numOfThreads);
      Thread thread1 = new Thread(task, "Task thread 1");
      Thread thread2 = new Thread(task, "Task thread 2");
      thread1.start();
      thread2.start();
      thread1.join();
      thread2.join();

      long duration = currentTimeMillis() - start;
      System.out.println(duration + " ms, " + VarHandleTask.HANDLE.get() + " iterations, blackhole: " + task.getBlackHole());
    }

    // https://shipilev.net/talks/jpoint-April2016-varhandles.pdf
  }
}
