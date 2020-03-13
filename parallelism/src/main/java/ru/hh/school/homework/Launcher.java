package ru.hh.school.homework;

import org.slf4j.Logger;
import ru.hh.school.homework.logic.FinderWords;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static java.lang.System.currentTimeMillis;
import static org.slf4j.LoggerFactory.getLogger;

public class Launcher {

  private static final Logger LOGGER = getLogger(Launcher.class);

  public static void main(String[] args) throws IOException {
      String directory;
      if (args.length == 0) {
          directory = new File("").getAbsolutePath();
          LOGGER.info("Directory not specified. The project directory is used: {}", directory);
      } else {
          directory = args[0];
      }

    if (!checkDirectory(directory)) return;
    long start = currentTimeMillis();
    Path path = Path.of(directory);
    FinderWords findWord = new FinderWords(path);
    findWord.execute();
    LOGGER.info("{} ms", currentTimeMillis() - start);
  }

  private static boolean checkDirectory(String directory) {
    if (directory == null) {
      LOGGER.error("The directory cannot be used!");
      return false;
    }

    File folder = new File(directory);
    if (!folder.exists() || folder.isFile()) {
      LOGGER.error("Invalid directory sent!");
      return false;
    }
    return true;
  }

}
