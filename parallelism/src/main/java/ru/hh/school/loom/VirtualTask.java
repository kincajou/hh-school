package ru.hh.school.loom;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import jdk.incubator.concurrent.StructuredTaskScope;
import ru.hh.school.parallelism.Runner;

public class VirtualTask {

  private final int tasks;
  private final StructuredTaskScope<Long> scope;

  public VirtualTask(int tasks, StructuredTaskScope<Long> scope) {
    this.tasks = tasks;
    this.scope = scope;
  }

  protected Long compute() throws ExecutionException, InterruptedException {
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

      return doFork(part1, part2);
    }

    return Runner.performCPUJob() + Runner.performIOJob();
  }

  private Long doFork(int part1, int part2) throws ExecutionException, InterruptedException {
    Future<Long> fork1 = scope.fork(() -> new VirtualTask(part1, scope).compute());
    Future<Long> fork2 = scope.fork(() -> new VirtualTask(part2, scope).compute());
    return fork1.get() + fork2.get();
  }


}
