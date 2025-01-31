package common;

public class Utils {

  private static long CONSUMED_CPU = System.nanoTime();

  public static long consumeCPUWithoutBarrier(int tokens) {
    long t = CONSUMED_CPU;
    for (long i = tokens; i > 0; i--) {
      t += (t * 0x5DEECE66DL + 0xBL + i) & (0xFFFFFFFFFFFFL);
    }
    if (t == 42) {
      CONSUMED_CPU += t;
    }

    return t;
  }
}
