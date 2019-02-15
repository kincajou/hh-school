package ru.hh.school.async.trueevil;

import org.slf4j.Logger;
import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.slf4j.LoggerFactory.getLogger;

public class TrueEvil {

  private static final Logger LOGGER = getLogger(TrueEvil.class);

  public static String delay(String data) {
    LOGGER.debug("[Evil mastermind] Hijacked the control of space and time, you shall wait");
    sleepUninterruptibly(20, SECONDS);
    LOGGER.debug("[Evil mastermind] Wait is over");
    return data;
  }

}
