package ru.hh.school.threadlocal;

import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class ThreadLocal1Basic {

  private static final Logger LOGGER = getLogger(ThreadLocal1Basic.class);

  // ThreadLocal with initialValue
  private static final ThreadLocal<String> THREAD_LOCAL = ThreadLocal.withInitial(() -> "Initial Value");

  static void main() {

    // basic get() - will return initialValue
    LOGGER.debug("Initial value: {}", THREAD_LOCAL.get());

    // basic set() - change value for current thread
    THREAD_LOCAL.set("Main Thread Value");
    LOGGER.debug("After set: {}", THREAD_LOCAL.get());

    // basic remove() - resets to initial value or null
    THREAD_LOCAL.remove();
    LOGGER.debug("After remove: {}", THREAD_LOCAL.get());

    // standard ThreadLocal without initial value returns null
    ThreadLocal<Integer> simpleThreadLocal = new ThreadLocal<>();
    LOGGER.debug("Simple ThreadLocal initial: {}", simpleThreadLocal.get());

    simpleThreadLocal.set(42);
    LOGGER.debug("Simple ThreadLocal after set: {}", simpleThreadLocal.get());
  }
}
