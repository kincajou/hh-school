package ru.hh.school.homework.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hh.school.homework.entity.PopularWords;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FinderWords {
    private static Logger LOGGER = LoggerFactory.getLogger(FinderWords.class);
    private static final ConcurrentHashMap<String, Long> wordsCount = new ConcurrentHashMap<>();
    private static List<PopularWords> popularWordsList = new ArrayList<>();
    private static ScanDirectory scanDirectory;

    public FinderWords(Path path) {
        scanDirectory = new ScanDirectory(path);
    }

    public void execute() throws IOException {
        popularWordsList = getPopularWords();

        popularWordsList.parallelStream()
                .flatMap(topWords -> topWords.getWords().stream())
                .filter(word -> !wordsCount.containsKey(word))
                .forEach(word -> wordsCount.put(word, getInternetCounts(word)));
    }

    public void print() {
        popularWordsList.parallelStream()
                .forEach(this::printResults);
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

    private void printResults(PopularWords popularWords) {
        for (String word : popularWords.getWords()) {
            LOGGER.info("Dir: {}, Word: {}, Results: {}", popularWords.getDirectory(), word, wordsCount.get(word));
        }
    }
}
