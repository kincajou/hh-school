package ru.hh.school.threadlocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class ThreadLocal4Cleanup {

  private static final Logger LOGGER = getLogger(ThreadLocal4Cleanup.class);

  private static final ThreadLocal<String> USER_CONTEXT = new ThreadLocal<>();

  static void main() throws InterruptedException {

    // Single thread pool to force reuse
    ExecutorService executor = Executors.newSingleThreadExecutor();

    LOGGER.debug("First task: setting context for User1");
    executor.submit(() -> {
      USER_CONTEXT.set("User1 Context");
      LOGGER.debug("In task 1, context is: {}", USER_CONTEXT.get());
      // Oops, forgot to remove()!
    });

//    executor.submit(() -> {
//      try {
//        USER_CONTEXT.set("User1 Context");
//        LOGGER.debug("In task 1, context is: {}", USER_CONTEXT.get());
//      }
//      finally {
//        USER_CONTEXT.remove();
//      }
//    });

    // Wait a bit to ensure task 1 is done
    TimeUnit.MILLISECONDS.sleep(100);

    LOGGER.debug("Second task: not setting context, just checking");
    executor.submit(() -> {
      // This might print User1 Context because the thread is reused!
      LOGGER.debug("In task 2, context is: {}", USER_CONTEXT.get());

      if ("User1 Context".equals(USER_CONTEXT.get())) {
        LOGGER.warn("DATA POLLUTION DETECTED! Context leaked from Task 1");
      }
    });

    executor.shutdown();
    executor.awaitTermination(1, TimeUnit.SECONDS);
  }
}
