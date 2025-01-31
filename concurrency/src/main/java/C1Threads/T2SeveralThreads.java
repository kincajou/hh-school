package C1Threads;

import common.Task;
import static java.lang.System.currentTimeMillis;
import java.util.List;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class T2SeveralThreads {

  // Several threads do the same amount of work in parallel.

  private static final Logger LOGGER = getLogger(T2SeveralThreads.class);

  public static void main(String[] args) throws InterruptedException {

    int iterations = 1_000_000_000;
    // how many cores do we have?
    int numOfThreads = Runtime.getRuntime().availableProcessors();
    System.out.println("num of threads: " + numOfThreads);

    while (true) {
      long start = currentTimeMillis();

      Task task = new Task(iterations / numOfThreads, false); // not precise, but ok to show the idea
      List<Thread> threads = IntStream.range(0, numOfThreads)
        // NOTE: name your threads!
          .mapToObj(i -> new Thread(task, "Task thread " + i))
          .toList();
      threads.forEach(Thread::start);
      for (Thread thread : threads) {
        thread.join();
      }
      // can't restart thread

      long duration = currentTimeMillis() - start;
      LOGGER.debug("{} ms", duration);
    }

  }
}
