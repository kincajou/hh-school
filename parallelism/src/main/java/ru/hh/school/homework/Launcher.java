package ru.hh.school.homework;

import ru.hh.school.homework.common.NaiveSearchTask;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.currentTimeMillis;
import static java.util.Collections.reverseOrder;
import static java.util.Map.Entry.comparingByValue;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

// 9000 ms in one thread and 400 ms using CachedThreadPool
public class Launcher {
  static ExecutorService executorService = Executors.newCachedThreadPool();
  public static void main(String[] args) throws IOException, InterruptedException {
    // Написать код, который, как можно более параллельно:
    // - по заданному пути найдет все "*.java" файлы
    // - для каждого файла вычислит 10 самых популярных слов (см. #naiveCount())
    // - соберет top 10 для каждой папки в которой есть хотя-бы один java файл
    // - для каждого слова сходит в гугл и вернет количество результатов по нему (см. #naiveSearch())
    // - распечатает в консоль результаты в виде:
    // <папка1> - <слово #1> - <кол-во результатов в гугле>
    // <папка1> - <слово #2> - <кол-во результатов в гугле>
    // ...
    // <папка1> - <слово #10> - <кол-во результатов в гугле>
    // <папка2> - <слово #1> - <кол-во результатов в гугле>
    // <папка2> - <слово #2> - <кол-во результатов в гугле>
    // ...
    // <папка2> - <слово #10> - <кол-во результатов в гугле>
    // ...
    //
    // Порядок результатов в консоли не обязательный.
    // При желании naiveSearch и naiveCount можно оптимизировать.


    // test our naive methods:
    long start = currentTimeMillis();
    Path rootDirPath = Path.of("d:\\projects\\work\\hh-school\\concurrency\\src");
    //Path rootDirPath = Path.of("E:\\GSG\\GRI\\frontend\\src\\");
    try (Stream<Path> stream = Files.walk(rootDirPath)) {
      Stream<Path> directoryStream = stream.filter(Files::isDirectory);
      long directorySearchDuration = currentTimeMillis() - start;
      System.out.printf("Directory search is completed in %d ms\r\n", directorySearchDuration);
      directoryStream
              .forEach(file -> {
                try {
                  directoryCount(file);
                } catch (InterruptedException e) {
                  throw new RuntimeException(e);
                }
              });
    }

    executorService.shutdown();

    // waits until all running tasks finish or timeout happens
    boolean finished = executorService.awaitTermination(10000L, TimeUnit.MILLISECONDS);

    if (!finished) {
      // interrupts all running threads, still no guarantee that everything finished
      executorService.shutdownNow();
    }

    long duration = currentTimeMillis() - start;
    System.out.printf("The task completed in %d ms", duration);
  }

  private static void directoryCount(Path path) throws InterruptedException {
    try (Stream<Path> stream = Files.list(path)) {

      Map<String, Long> result = stream
              .filter(Files::isRegularFile)
              .filter(file -> file.toString().endsWith(".java"))
              .map(file -> naiveCount(file))
              .map(Map::entrySet)
              .flatMap(Collection::stream)
              .collect(groupingBy(Entry::getKey, summarizingLong(Entry::getValue)))
              .entrySet()
              .stream()
              .collect(Collectors.toMap(Entry::getKey, wordCount -> wordCount.getValue().getSum()))
              .entrySet()
              .stream()
              .sorted(comparingByValue(reverseOrder()))
              .limit(10)
              .collect(Collectors.toMap(Entry::getKey, Entry::getValue));


      result.forEach((key, value) -> executorService.execute(new NaiveSearchTask(key, path)));

    } catch (IOException e) {
      System.out.println("It is impossible to get count value form google");
    }
  }

  private static Map<String, Long> naiveCount(Path path) {
    try {
      return Files.lines(path)
              .flatMap(line -> Stream.of(line.split("[^a-zA-Z0-9]")))
              .filter(word -> word.length() > 3)
              .collect(groupingBy(identity(), counting()))
              .entrySet()
              .stream()
              .sorted(comparingByValue(reverseOrder()))
              .limit(10)
              .collect(toMap(Entry::getKey, Entry::getValue));
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}

