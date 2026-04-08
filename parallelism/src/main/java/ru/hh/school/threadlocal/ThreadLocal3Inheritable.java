package ru.hh.school.threadlocal;

import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class ThreadLocal3Inheritable {

  private static final Logger LOGGER = getLogger(ThreadLocal3Inheritable.class);

  private static final ThreadLocal<String> NORMAL_THREAD_LOCAL = new ThreadLocal<>();
  private static final InheritableThreadLocal<String> INHERITABLE_THREAD_LOCAL = new InheritableThreadLocal<>();

  static void main() throws InterruptedException {

    NORMAL_THREAD_LOCAL.set("Parent Context (Normal)");
    INHERITABLE_THREAD_LOCAL.set("Parent Context (Inheritable)");

    LOGGER.debug("Normal={}, inheritable={}", NORMAL_THREAD_LOCAL.get(), INHERITABLE_THREAD_LOCAL.get());

    Thread child = new Thread(
        () -> {
          LOGGER.debug("Normal={}, inheritable={}", NORMAL_THREAD_LOCAL.get(), INHERITABLE_THREAD_LOCAL.get());

          INHERITABLE_THREAD_LOCAL.set("Child Modified Context");
          LOGGER.debug("Modified inheritable: {}", INHERITABLE_THREAD_LOCAL.get());
        }, "Child"
    );

    child.start();
    child.join();

    LOGGER.debug("After child join: normal={}, inheritable={}", NORMAL_THREAD_LOCAL.get(), INHERITABLE_THREAD_LOCAL.get());

    NORMAL_THREAD_LOCAL.remove();
    INHERITABLE_THREAD_LOCAL.remove();
  }
}
