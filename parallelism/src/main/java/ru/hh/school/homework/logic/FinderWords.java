package ru.hh.school.homework.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hh.school.homework.entity.PopularWords;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FinderWords {
    private static final Logger LOGGER = LoggerFactory.getLogger(FinderWords.class);
    private ScanDirectory scanDirectory;

    public FinderWords(Path path) {
        scanDirectory = new ScanDirectory(path);
    }

    public void execute() throws IOException {
        List<PopularWords> popularWordsList = getPopularWords();
        Map<String, Long> wordsCount = popularWordsList.parallelStream()
                .flatMap(topWords -> topWords.getWords().stream())
                .distinct()
                .collect(Collectors.toConcurrentMap(Function.identity(), this::getInternetCounts));
        print(popularWordsList, wordsCount);
    }

    private List<PopularWords> getPopularWords() throws IOException {
        return scanDirectory.getDirectories().parallelStream()
                .map(scanDirectory::getPopularWords)
                .filter(words -> !words.getWords().isEmpty())
                .collect(Collectors.toList());
    }

    private long getInternetCounts(String query) {
           return SimpleWordCounter.naiveSearch(query);
    }

    private void print(List<PopularWords> popularWordsList, Map<String, Long> wordsCount) {
        popularWordsList.parallelStream()
                .forEach(popularWords -> printResults(popularWords, wordsCount));
    }

    private void printResults(PopularWords popularWords, Map<String, Long> wordsCount) {
        popularWords.getWords()
                .parallelStream()
                .forEach(word -> LOGGER.info("Dir: {}, Word: {}, Results: {}", popularWords.getDirectory(), word, wordsCount.get(word)));
    }
}
