package ru.hh.school.loom;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.atomic.LongAdder;
import ru.hh.school.parallelism.Computation;

@SuppressWarnings("preview")
public class LoomForkJoinComputation implements Computation {

  @Override
  public long compute(int tasks) throws ExecutionException, InterruptedException {
    LongAdder resultAccumulator = new LongAdder();

    try (var scope = StructuredTaskScope.open()) {
      new VirtualTask(tasks, resultAccumulator, scope).compute();
      scope.join();
      return resultAccumulator.longValue();
    }
  }
}
