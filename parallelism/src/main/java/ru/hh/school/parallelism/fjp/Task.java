package ru.hh.school.parallelism.fjp;

import java.util.concurrent.RecursiveTask;
import ru.hh.school.parallelism.Runner;

public class Task extends RecursiveTask<Long> {

  private final int tasks;

  public Task(int tasks) {
    this.tasks = tasks;
  }

  @Override
  protected Long compute() {

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

  private long doFork(int part1, int part2) {
    Task task1 = new Task(part1);
    Task task2 = new Task(part2);

    invokeAll(task1, task2);

    return task1.join() + task2.join();
  }

}
