package C3Visibility;

import common.Task;
import common.Utils;
import static java.lang.System.currentTimeMillis;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class V4VisibilityByVarHandle {

  // VarHandle allows to set reading and writing guarantees per operation with any field
  //
  // - Plain reads and writes guarantee bitwise atomicity for references and primitives under 32 bits.
  //   Also, they impose no ordering constraints with respect to the other traits.
  // - Opaque operations are bitwise atomic and coherently ordered with respect to access to the same variable.
  // - Acquire and Release operations obey Opaque properties.
  //   Also, Acquire reads will be ordered only after matching Release mode writes.
  // - Volatile operations are fully ordered with respect to each other.
  //
  // https://www.baeldung.com/java-variable-handles
  // https://shipilev.net/talks/jpoint-April2016-varhandles.pdf

  private static final Logger LOGGER = getLogger(V4VisibilityByVarHandle.class);

  static class VarHandleTask extends Task {

    private int actualIterations = 0;
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

    int iterations = 100_000_000;

    while (true) {
      long start = currentTimeMillis();

      VarHandleTask task = new VarHandleTask(iterations);
      Thread thread = new Thread(task, "Task thread");
      thread.start();
      while (true) {
        if ((int) task.handle.getVolatile(task) >= iterations) {
          break;
        }
        Utils.consumeCPUWithoutBarrier(100);
      }

      long duration = currentTimeMillis() - start;
      LOGGER.debug("{} ms, value: {}", duration, task.handle.get(task));
    }
  }
}
