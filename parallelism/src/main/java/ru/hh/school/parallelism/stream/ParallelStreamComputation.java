package ru.hh.school.parallelism.stream;

import java.util.stream.IntStream;
import ru.hh.school.parallelism.Computation;
import ru.hh.school.parallelism.Runner;

public class ParallelStreamComputation implements Computation {

  public long compute(int tasks) {
    return IntStream
      .range(0, tasks)
      .parallel()
      .mapToLong(i -> Runner.performCPUJob())
      .map(result -> result + Runner.performIOJob())
      .sum();
  }

}
