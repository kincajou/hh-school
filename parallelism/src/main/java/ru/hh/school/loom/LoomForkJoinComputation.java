package ru.hh.school.loom;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.atomic.LongAdder;
import ru.hh.school.parallelism.Computation;

public class LoomForkJoinComputation implements Computation {

  @Override
  public long compute(int tasks) throws ExecutionException, InterruptedException {
    LongAdder resultAccumulator = new LongAdder();

    try (StructuredTaskScope<Void> scope = new StructuredTaskScope<>()) {
      new VirtualTask(tasks, resultAccumulator, scope).compute();
      scope.join();
      return resultAccumulator.longValue();
    }
  }
}
