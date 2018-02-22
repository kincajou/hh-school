package C1Threads;

import common.Task;

import java.util.List;
import java.util.stream.IntStream;

import static java.lang.System.currentTimeMillis;
import static java.util.stream.Collectors.toList;

public class T2SeveralThreads {

  // Several threads does the same amount of work in parallel.

  public static void main(String[] args) throws InterruptedException {

    int iterations = 1_000_000_000;
    // how much cores do we have?
    int numOfThreads = Runtime.getRuntime().availableProcessors();

    while (true) {
      long start = currentTimeMillis();

      Task task = new Task(iterations / numOfThreads); // not precise, but ok to show the idea
      List<Thread> threads = IntStream.range(0, numOfThreads)
        // NOTE: name your threads!
          .mapToObj(i -> new Thread(task, "thread" + i))
          .collect(toList());
      threads.forEach(Thread::start);
      for (Thread thread : threads) {
        thread.join();
      }
      // can't restart thread

      long duration = currentTimeMillis() - start;
      System.out.println(duration + " ms, " + task.getBlackHole());
    }

  }
}
