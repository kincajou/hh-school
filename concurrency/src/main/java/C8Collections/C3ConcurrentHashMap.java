package C8Collections;

import common.Task;
import static java.lang.System.currentTimeMillis;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class C3ConcurrentHashMap {

  private static final Logger LOGGER = getLogger(C3ConcurrentHashMap.class);

  record CachedValue(int id, int value) {
  }

  // same code as previous example except this string
  private static final Map<Integer, CachedValue> CACHE = new ConcurrentHashMap<>();

  // use ConcurrentSkipListMap as substitute for SortedMap
  // use CopyOnWriteArraySet as substitute for HashSet
  // and CopyOnWriteArrayList instead of ArrayList

  static class WritingTask extends Task {

    private final AtomicInteger iteration = new AtomicInteger();

    public WritingTask(int iterations) {
      super(iterations, false);
    }

    @Override
    protected void onIteration() {
      int id = iteration.incrementAndGet();
      int value = ThreadLocalRandom.current().nextInt();
      CACHE.put(id, new CachedValue(id, value));
    }
  }

  static class ReadingTask extends Task {

    private final AtomicInteger iteration = new AtomicInteger();
    private long blackhole;

    public ReadingTask(int iterations) {
      super(iterations, false);
    }

    @Override
    protected void onIteration() {
      int value = iteration.incrementAndGet();
      CachedValue cachedValue = CACHE.get(value);
      if (cachedValue != null) {
        blackhole += cachedValue.value;
      }
    }
  }

  public static void main(String[] args) throws InterruptedException {
    while(true) doCycle();
  }

  private static void doCycle() throws InterruptedException {
    int iterations = 500_000;

    CACHE.clear();

    WritingTask writingTask = new WritingTask(iterations);
    ReadingTask readingTask = new ReadingTask(iterations);

    List<Thread> writingThreads = IntStream.range(0, 8).mapToObj(i -> new Thread(writingTask)).toList();
    List<Thread> readingThreads = IntStream.range(0, 8).mapToObj(i -> new Thread(readingTask)).toList();

    long start = currentTimeMillis();

    writingThreads.forEach(Thread::start);
    readingThreads.forEach(Thread::start);
    for (Thread thread : writingThreads) {
      thread.join();
    }
    for (Thread thread : readingThreads) {
      thread.join();
    }
    long duration = currentTimeMillis() - start;

    LOGGER.debug("Cache filled in {}ms, size is {}, blackhole is {}", duration, CACHE.size(), readingTask.blackhole);
  }
}
