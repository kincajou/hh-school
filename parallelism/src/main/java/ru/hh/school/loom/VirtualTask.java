package ru.hh.school.loom;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.atomic.LongAdder;
import ru.hh.school.parallelism.Runner;

public class VirtualTask {

  private final int tasks;
  private final LongAdder resultAccumulator;
  private final StructuredTaskScope<Void> scope;

  public VirtualTask(int tasks, LongAdder resultAccumulator, StructuredTaskScope<Void> scope) {
    this.tasks = tasks;
    this.resultAccumulator = resultAccumulator;
    this.scope = scope;
  }

  protected void compute() throws ExecutionException, InterruptedException {
    if (tasks > 1) {
      int part1, part2;
      if (tasks % 2 == 0) {
        part1 = tasks / 2;
        part2 = tasks / 2;
      }
      else {
        part1 = Math.floorDiv(tasks, 2);
        part2 = part1 + 1;
      }

      doFork(part1, part2);
      return;
    }

    resultAccumulator.add(Runner.performCPUJob() + Runner.performIOJob());
  }

  private void doFork(int part1, int part2) {
    scope.fork(() -> {
      new VirtualTask(part1, resultAccumulator, scope).compute();
      return null;
    });
    scope.fork(() -> {
      new VirtualTask(part2, resultAccumulator, scope).compute();
      return null;
    });
  }


}
