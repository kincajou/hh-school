package ru.hh.school.parallelism.sequential;

import java.util.stream.IntStream;
import ru.hh.school.parallelism.Computation;
import ru.hh.school.parallelism.Runner;

public class SequentialComputation implements Computation {

  @Override
  public long compute(int tasks) {
    return IntStream.range(0, tasks).mapToLong(i -> Runner.performCPUJob()).map(result -> result + Runner.performIOJob()).sum();
  }

}
