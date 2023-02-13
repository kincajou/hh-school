package ru.hh.school.homework;

import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.slf4j.LoggerFactory.getLogger;

public class SearchDirectoryService {
    private static final Logger LOGGER = getLogger(SearchDirectoryService.class);
    Map<Path, List<Path>> paths;
    Path directory;
    String filterWord;

    public SearchDirectoryService(Path directory, String filterWord) {
        this.directory = directory;
        this.filterWord = filterWord;
    }

    public Map<Path, List<Path>> searchFiles() {
        try (Stream<Path> pathStream = Files.walk(directory)) {
            paths = pathStream
                    .toList()
                    .parallelStream()
                    .filter(Files::isRegularFile)
                    .filter(file -> file.getFileName().toString().endsWith(filterWord))
                    .collect(
                            Collectors.groupingBy(Path::getParent, HashMap::new,
                                    Collectors.mapping(Function.identity(), Collectors.toList())));
            return paths;

        } catch (IOException e) {
            LOGGER.warn("can not traversing files in a directory", e);
        }
        return Collections.emptyMap();
    }
}
