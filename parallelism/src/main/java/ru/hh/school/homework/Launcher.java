package ru.hh.school.homework;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.reverseOrder;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Launcher {
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
    //  testCount();
    //  testSearch();
    private static List<Path> listJavaFile;
    private static final Path PATH = Path.of("C:\\Users\\Artem\\IdeaProjects\\hh-school1\\parallelism\\src\\main\\java");
    private static final String EXTENSION = ".java";

    public static void main(String[] args) throws IOException {
        ConcurrentMap<String, Map<String, Long>> listFolderAndWord = new ConcurrentHashMap<>();
        ConcurrentMap<String, ArrayList<String>> result = new ConcurrentHashMap<>();

        getListJavaFile(PATH, EXTENSION);

        listJavaFile.parallelStream().forEach(file -> {
            String parent = file.getParent().toString();
            String folderName = " [" + parent + "]";
            mergeCountWordInFolder(listFolderAndWord, file, folderName);
        });

        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Callable<Object>> tasks = new ArrayList<>();
        try {
            listFolderAndWord.forEach((folder, mapOfWord) -> mapOfWord.forEach((word, count) -> tasks.add(() -> {
                try {
                    result.computeIfAbsent("\n" + "Папка: " + folder, value -> new ArrayList<>()).add("\n" + " Слово для поиска:" + word + "," + " КолВо результатов в Google:" + naiveSearch(word));
                } catch (IOException e) {
                    System.out.println(e);
                    throw new RuntimeException(e);
                }
                return null;
            })));
            executorService.invokeAll(tasks);
            System.out.println(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }
    private static void mergeCountWordInFolder(Map<String, Map<String, Long>> listFolderAndWord, Path file, String folderName) {
        listFolderAndWord.merge(folderName, naiveCount(file), (map1, map2) -> {
            map2.forEach((k, v) -> map1.merge(k, v, Long::sum));
            return map1.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(reverseOrder()))
                    .limit(10)
                    .collect(LinkedHashMap::new,
                            (map, item) -> map.put(item.getKey(), item.getValue()),
                            Map::putAll);
        });
    }


    private static Map<String, Long> naiveCount(Path path) {
        try {
            return Files.lines(path)
                    .flatMap(line -> Stream.of(line.split("[^a-zA-Z0-9]")))
                    .filter(word -> word.length() > 3)
                    .collect(groupingBy(identity(), counting()))
                    .entrySet()
                    .stream().sorted(Map.Entry.comparingByValue(reverseOrder()))
                    .limit(10)
                    .collect(LinkedHashMap::new,                           // Supplier LinkedHashMap to keep the order
                            (map, item) -> map.put(item.getKey(), item.getValue()),  // Accumulator
                            Map::putAll);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static long naiveSearch(String query) throws IOException {
        Document document = Jsoup //
                .connect("https://www.google.com/search?q=" + query) //
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.116 Safari/537.36") //
                .get();

        Element divResultStats = document.select("#result-stats").first();
        String text = divResultStats.text();
        String resultsPart = text.substring(0, text.indexOf('('));
        return Long.parseLong(resultsPart.replaceAll("[^0-9]", ""));
    }

    public static void getListJavaFile(Path path, String fileExtension)
            throws IOException {

        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Path must be a directory!");
        }

        try (Stream<Path> walk = Files.walk(path)) {
            listJavaFile = walk
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().endsWith(fileExtension))
                    .collect(Collectors.toList());
        }
    }
}