package ru.hh.school.parallelism.fjp;

import java.util.concurrent.ForkJoinPool;
import ru.hh.school.parallelism.Computation;

public class ForkJoinPoolComputation implements Computation {

  public long compute(int tasks) {
    // ForkJoinPool pool = new ForkJoinPool();
    ForkJoinPool pool = ForkJoinPool.commonPool();
    Task task = new Task(tasks);
    return pool.invoke(task);
  }

}
