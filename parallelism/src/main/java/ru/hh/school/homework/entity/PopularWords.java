package ru.hh.school.homework.entity;

import java.nio.file.Path;
import java.util.List;

public class PopularWords {
    private Path directory;
    private List<String> words;

    public PopularWords(Path directory, List<String> words) {
        this.directory = directory;
        this.words = words;
    }

    public Path getDirectory() {
        return directory;
    }

    public List<String> getWords() {
        return words;
    }

    @Override
    public String toString() {
        return "directory = " + directory +
                ", words = " + words;
    }
}
