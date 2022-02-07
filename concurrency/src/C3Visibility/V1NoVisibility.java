package C3Visibility;

import common.Task;

import static java.lang.System.currentTimeMillis;

public class V1NoVisibility {

  // Does it always stop?
  // Notice number of iterations.

  static class NoVisibilityTask extends Task {

    NoVisibilityTask(int iterations) {
      super(iterations);
    }

    int actualIterations = 0;

    @Override
    protected void onIteration() {
      actualIterations++;
    }
  }

  public static void main(String[] args) {

    int iterations = 10_000;

    long start = currentTimeMillis();

    NoVisibilityTask task = new NoVisibilityTask(iterations);
    Thread thread = new Thread(task, "Task thread");
    thread.start();

    while (true) {
      // can we put == here?
      if (task.actualIterations >= iterations) {
        break;
      }
      // will reading second time help?

      //if (task.actualIterations >= iterations) {
      //   break;
      // }

      // will sysout help?

      // System.out.println(task.actualIterations);

      // will another OS help?
    }

    long duration = currentTimeMillis() - start;
    System.out.println(duration + " ms, blackhole: " + task.getBlackHole());
  }

  // - sometime this will stop, sometime it will not - that depends on many factors, but it is definitely is unstable.
  // actualIterations field is again a shared state between "Task thread" that writes to it and main thread that reads it.
  // Since the field is not protected against concurrency, caches are not synchronized in controlled way.
  // Using == instead of >= makes things worse.
  // Reading it second time somewhat but not greatly increase the chance that something synchronizes caches for us and main thread will see correct value.
  // System.out.println has synchronization inside and acts as barrier after which caches are synhronized and we will always see correct value.
  // Relying on System.out.println (or other barrier calls) to make our code work correctly is called piggybacking, and it is advised to avoid it
  // since there are no guarantees it will work consistently across different jvm versions and can break because of simple refactoring.
}
