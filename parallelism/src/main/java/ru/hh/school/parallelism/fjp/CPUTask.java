package ru.hh.school.parallelism.fjp;

import java.util.concurrent.RecursiveTask;
import ru.hh.school.parallelism.Runner;

public class CPUTask extends RecursiveTask<Long> {

  @Override
  protected Long compute() {
    return Runner.performCPUJob();
  }

}
