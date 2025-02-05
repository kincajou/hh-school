package ru.hh.school.loom;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

// Scoped values is still a preview feature, needs --enable-preview jvm argument and api is subject to change

// ThreadLocal is heavy, ScopedValue is lightweight
// ThreadLocal lives till the end of thread, ScopedValue lives till the end of call scope
// ThreadLocal is mutable, ScopedValue is immutable (but can be rebound)
// Both are inheritable (ThreadLocal is inherited via InheritableThreadLocal)
@SuppressWarnings("preview")
public class Loom4ScopedValue {

  private static final Logger LOGGER = getLogger(Loom4ScopedValue.class);

  public static final class User {
    public Integer id;
    public String name;

    public User(Integer id, String name) {
      this.id = id;
      this.name = name;
    }
  }

  public static final ScopedValue<User> CURRENT_USER = ScopedValue.newInstance();

  public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
    platformThreadExample();
    virtualThreadExample();
    inheritanceViaStructuredTaskScopeExample();
    rebindingExample();
    noValueExample();
    belongsToThreadExample();
  }

  private static Integer logCurrentUser() {
    User user = CURRENT_USER.get();
    if (user == null) {
      LOGGER.debug("User is null");
      return null;
    }
    else {
      LOGGER.debug("User id: {}, name: {}", CURRENT_USER.get().id, CURRENT_USER.get().name);
      return user.id;
    }
  }

  private static void platformThreadExample() throws InterruptedException {
    Thread
        .ofPlatform()
        .name("platformThreadExample")
        .start(() -> ScopedValue.where(CURRENT_USER, new User(1, "Duke")).run(Loom4ScopedValue::logCurrentUser))
        .join();
    // ScopedValue.runWhere(...); returns nothing
    // ScopedValue.callWhere(...); returns something
    // ScopedValue.where(...).where(...).run(...); allows to bind multiple values builder-style
  }

  private static void virtualThreadExample() throws InterruptedException {
    Thread
        .ofVirtual()
        .name("virtualThreadExample")
        .start(() -> ScopedValue.where(CURRENT_USER, new User(2, "0xCafeBabe")).run(Loom4ScopedValue::logCurrentUser))
        .join();
  }

  private static void inheritanceViaStructuredTaskScopeExample() {
    ScopedValue.runWhere(CURRENT_USER, new User(3, "Duchess"), () -> {
      try (var scope = new StructuredTaskScope<Integer>()) {
        scope.fork(Loom4ScopedValue::logCurrentUser);
        scope.fork(Loom4ScopedValue::logCurrentUser);
        try {
          scope.join();
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    });
  }

  private static void rebindingExample() throws InterruptedException {
    Thread.ofVirtual().name("rebindingExample").start(() -> ScopedValue.runWhere(
        CURRENT_USER, new User(4, "Duke"), () -> {
          logCurrentUser();
          ScopedValue.runWhere(CURRENT_USER, new User(5, "Duchess"), Loom4ScopedValue::logCurrentUser);
          logCurrentUser();
        }
    )).join();
  }

  private static void noValueExample() {
    try {
      logCurrentUser();
    }
    catch (RuntimeException e) {
      LOGGER.error("Failed", e);
    }
  }

  private static void belongsToThreadExample() {
    ScopedValue.runWhere(
        CURRENT_USER,
        new User(6, "Ductape"),
        () -> Thread.ofVirtual().name("belongsToThreadExample").start(Loom4ScopedValue::logCurrentUser)
    );
  }
}
