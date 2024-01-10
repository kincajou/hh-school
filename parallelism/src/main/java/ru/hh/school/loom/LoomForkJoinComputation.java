package ru.hh.school.loom;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import ru.hh.school.parallelism.Computation;

public class LoomForkJoinComputation implements Computation {

  @Override
  public long compute(int tasks) throws ExecutionException, InterruptedException {
    try (StructuredTaskScope<Long> scope = new StructuredTaskScope<>()) {
      StructuredTaskScope.Subtask<Long> future = scope.fork(() -> new VirtualTask(tasks, scope).compute());
      scope.join();
      return future.get();
    }
  }
}
