package C3Visibility;

import common.Task;
import static java.lang.System.currentTimeMillis;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class V4VisibilityByVarHandle {

  // VarHandle allows to set reading and writing guarantees per operation with any field
  // https://shipilev.net/talks/jpoint-April2016-varhandles.pdf

  static class VarHandleTask extends Task {

    int actualIterations = 0;
    final VarHandle handle;

    VarHandleTask(int iterations) {
      super(iterations);
      try {
        handle = MethodHandles
            .lookup()
            .findVarHandle(VarHandleTask.class, "actualIterations", int.class);
      } catch (NoSuchFieldException | IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    protected void onIteration() {
      actualIterations++;
    }
  }

  public static void main(String[] args) throws InterruptedException {

    int iterations = 1_000_000_000;

    while (true) {
      long start = currentTimeMillis();

      VarHandleTask task = new VarHandleTask(iterations);
      Thread thread = new Thread(task, "Task thread");
      thread.start();
      while (true) {
        // .get method on VarHandle works as if the field was declared volatile
        if ((int) task.handle.get(task) >= iterations) {
          break;
        }
        Thread.sleep(10L);
      }

      long duration = currentTimeMillis() - start;
      System.out.println(duration + " ms, blackhole: " + task.getBlackHole() + " " + task.handle.get(task));
    }
  }
}
