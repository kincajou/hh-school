package ru.hh.school.homework.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hh.school.homework.entity.PopularWords;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FinderWords {
    private static final Logger LOGGER = LoggerFactory.getLogger(FinderWords.class);
    private static final ConcurrentHashMap<String, Long> WORDS_COUNT = new ConcurrentHashMap<>();
    private static List<PopularWords> POPULAR_WORD_LIST = new ArrayList<>();
    private static ScanDirectory SCAN_DIRECTORY;

    public FinderWords(Path path) {
        SCAN_DIRECTORY = new ScanDirectory(path);
    }

    public void execute() throws IOException {
        POPULAR_WORD_LIST = getPopularWords();

        POPULAR_WORD_LIST.parallelStream()
                .flatMap(topWords -> topWords.getWords().stream())
                .filter(word -> !WORDS_COUNT.containsKey(word))
                .forEach(word -> WORDS_COUNT.put(word, getInternetCounts(word)));
    }

    public void print() {
        POPULAR_WORD_LIST.parallelStream()
                .forEach(this::printResults);
    }

    private List<PopularWords> getPopularWords() throws IOException {
        return SCAN_DIRECTORY.getDirectories().parallelStream()
                .map(SCAN_DIRECTORY::getPopularWords)
                .filter(words -> !words.getWords().isEmpty())
                .collect(Collectors.toList());
    }

    private long getInternetCounts(String query) {
           return SimpleWordCounter.naiveSearch(query);
    }

    private void printResults(PopularWords popularWords) {
        for (String word : popularWords.getWords()) {
            LOGGER.info("Dir: {}, Word: {}, Results: {}", popularWords.getDirectory(), word, WORDS_COUNT.get(word));
        }
    }
}
