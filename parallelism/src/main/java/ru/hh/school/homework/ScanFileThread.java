package ru.hh.school.homework;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import static java.util.Collections.reverseOrder;
import static java.util.Map.Entry.comparingByValue;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

public class ScanFileThread extends Thread {

  private List<Path> files;
  private Path folder;

  public ScanFileThread(Path folder, List<Path> files) {
    this.folder = folder;
    this.files = files;
  }

  @Override
  public void run() {
    List<String> top10Words = top10Words(files);
    new GoogleThread(folder, top10Words.subList(0, 5)).start();
    new GoogleThread(folder, top10Words.subList(5, 10)).start();
  }

  private static Map<String, Long> naiveCount(Path path) {
    try {
      return Files.lines(path)
          .flatMap(line -> Stream.of(line.split("[^a-zA-Z\\d]")))
          .filter(word -> word.length() > 3)
          .collect(groupingBy(identity(), counting()))
          .entrySet()
          .stream()
          .sorted(comparingByValue(reverseOrder()))
          .limit(10)
          .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static List<String> top10Words(List<Path> files) {
    return files.stream()
        .flatMap(file -> naiveCount(file).entrySet().stream())
        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum))
        .entrySet()
        .stream()
        .sorted(comparingByValue(reverseOrder()))
        .limit(10)
        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue))
        .keySet().stream().toList();
  }

}
