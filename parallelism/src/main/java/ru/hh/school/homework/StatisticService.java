package ru.hh.school.homework;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.reverseOrder;
import static java.util.Map.Entry.comparingByValue;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;
import static org.slf4j.LoggerFactory.getLogger;

public class StatisticService {
    SearchDirectoryService searchDirectoryService;
    private static final ExecutorService FORK_JOIN_EXECUTOR = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
    private static final Logger LOGGER = getLogger(SearchDirectoryService.class);
    private final Map<String, Long> countGoogleResultByWord = new ConcurrentHashMap<>();

    public StatisticService(SearchDirectoryService searchDirectoryService) {
        this.searchDirectoryService = searchDirectoryService;
    }

    public static CompletableFuture<Map<String, Integer>> countWords(List<Path> pathFiles,
                                                                     ExecutorService executorService) {
        return CompletableFuture.supplyAsync(() -> {
            List<CompletableFuture<Void>> futuresCountWords = new ArrayList<>();
            Map<String, Long> countByWord = new ConcurrentHashMap<>();
            for (Path path : pathFiles) {
                futuresCountWords.add(CompletableFuture.supplyAsync(() -> naiveCount(path), executorService)
                        .thenAccept(map -> mergeMap(map, countByWord))
                );
            }
            futuresCountWords.forEach(CompletableFuture::join);
            LongAdder count = new LongAdder();
            return countByWord.entrySet().stream()
                    .sorted(comparingByValue(reverseOrder()))
                    .limit(10)
                    .collect(Collectors.toMap(Map.Entry::getKey, v -> {
                        count.increment();
                        return count.intValue();
                    }));
        });
    }

    public static void mergeMap(Map<String, Long> nativeCountByWord, Map<String, Long> allCountByWord) {
        nativeCountByWord.forEach((key, value) -> allCountByWord.merge(key, value, Long::sum));
    }

    public void calculateStatistic() {
        Map<Path, List<Path>> paths = searchDirectoryService.searchFiles();
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        paths.forEach((k, v) -> {
                    CompletableFuture<Void> feature = countWords(v, FORK_JOIN_EXECUTOR)
                            .thenComposeAsync(s -> searchWords(s, countGoogleResultByWord, k.getFileName()), FORK_JOIN_EXECUTOR)
                            .thenAccept(StatisticService::printStatistics);
                    futures.add(feature);
                }
        );
        futures.forEach(CompletableFuture::join);
    }

    private static CompletableFuture<List<Statistic>>
    searchWords(Map<String, Integer> topByword, final Map<String, Long> countGoogleResultByWord,
                Path folderPath) {
        List<Statistic> statistics = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : topByword.entrySet()) {
            try {
                if (countGoogleResultByWord.get(entry.getKey()) == null) {
                    long countSearchGoogleResult = naiveSearch(entry.getKey());
                    countGoogleResultByWord.put(entry.getKey(), countSearchGoogleResult);
                }
                Statistic statistic =
                        new Statistic(folderPath, entry.getKey(), entry.getValue(),
                                countGoogleResultByWord.get(entry.getKey()));
                statistics.add(statistic);

            } catch (Exception ex) {
                LOGGER.warn("can not find search result in google by word {}", entry.getKey(), ex);
            }
        }

        return CompletableFuture.completedFuture(statistics);
    }

    private static void printStatistics(List<Statistic> statistics) {
        for (Statistic statistic : statistics) {
            System.out.printf("%-20s %-25s #%-2d %d\n", statistic.getPathDir(), statistic.getWord(), statistic.getTop(),
                    statistic.getNumberOfResultsGoogle());


        }
    }

    private static long naiveSearch(String query) throws InterruptedException {
        AtomicInteger tryCounter = new AtomicInteger(3);
        String text = "";
        String url = "https://www.google.com/search?q=" + query;
        do {
            Document document = null;
            Jsoup.connect(url);
            try {
                document = Jsoup //
                        .connect(url) //
                        .newRequest()
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.116 Safari/537.36") //
                        .get();
            } catch (IOException e) {
                TimeUnit.SECONDS.sleep(5);
                LOGGER.warn("try#{} connect to url: {}", tryCounter, url, e);
            }
            if (document != null) {
                Element divResultStats = document.select("div#result-stats").first(); // не всегда находит почему-то по slim_appbar
                text = divResultStats.text();
            }

        } while (text.length() == 0 && tryCounter.decrementAndGet() > 0);

        String resultsPart = text.substring(0, text.indexOf('('));
        return Long.parseLong(resultsPart.replaceAll("[^0-9]", ""));
    }

    private static Map<String, Long> naiveCount(Path path) {
        try (Stream<String> linesStream = Files.lines(path)) {
            return linesStream
                    .flatMap(line -> Stream.of(line.split("[^a-zA-Z0-9]")))
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

    public void shutDown() {
        FORK_JOIN_EXECUTOR.shutdown();
    }
}
