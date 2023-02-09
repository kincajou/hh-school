package ru.hh.school.homework;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static java.lang.System.currentTimeMillis;
import static java.util.Collections.reverseOrder;
import static java.util.Map.Entry.comparingByValue;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

//9076 ms
public class L1LauncherSequential {

  public static void main(String[] args) throws IOException {
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
      stream.filter(Files::isDirectory)
              .forEach(file -> directoryCount(file));
    } catch (IOException e) {
      System.out.println("It is impossible to read directories info");
    }
    long duration = currentTimeMillis() - start;
    System.out.printf("The task completed in %d ms", duration);
    //testCount();
    //testSearch();
  }

  private static void directoryCount(Path path) {
    try (Stream<Path> stream = Files.list(path)) {

      Map<String, Long> result = stream
              //.peek(System.out::println)
              .filter(Files::isRegularFile)
              .filter(file -> file.toString().endsWith(".java"))
              .map(file -> naiveCount(file))
              //.peek(System.out::println)
              .map(Map::entrySet)
              .flatMap(Collection::stream)
              .collect(groupingBy(Entry::getKey, summarizingLong(Entry::getValue)))
              .entrySet()
              .stream()
              .collect(Collectors.toMap(Entry::getKey, v -> v.getValue().getSum()))
              .entrySet()
              .stream()
              .sorted(comparingByValue(reverseOrder()))
              .limit(10)
              .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

      result.forEach((key, value) -> printCount(path, key));
    } catch (IOException e) {
      System.out.println("It is impossible to get count value form google");
    }
  }

  private static void printCount(Path directoryPath, String wordToCount) {
    try {
      System.out.printf("%s - %s - %d\r\n", directoryPath, wordToCount, naiveSearchMock(wordToCount));
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
}

  private static void testCount() {
    Path path = Path.of("d:\\projects\\work\\hh-school\\parallelism\\src\\main\\java\\ru\\hh\\school\\parallelism\\Runner.java");
    System.out.println(naiveCount(path));
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

  private static void testSearch() throws IOException {
    System.out.println(naiveSearch("public"));
  }

  private static long naiveSearch(String query) throws IOException {
    Document document = Jsoup //
      .connect("https://www.google.com/search?q=" + query) //
      .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.116 Safari/537.36") //
      .get();

    Element divResultStats = document.select("div#result-stats").first(); //div#result-stats
    String text = divResultStats.text();
    String resultsPart = text.substring(0, text.indexOf('('));
    return Long.parseLong(resultsPart.replaceAll("[^0-9]", ""));
  }
  private static long naiveSearchMock(String query) throws IOException {
    try {
      Thread.sleep(90L);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    return ThreadLocalRandom.current().nextInt();
  }

}
