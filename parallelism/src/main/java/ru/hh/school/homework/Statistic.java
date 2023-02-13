package ru.hh.school.homework;

import java.nio.file.Path;

public class Statistic {
    private Path pathDir;
    String word;

    long top;
    long numberOfResultsGoogle;

    public Statistic(Path pathDir, String word, long top, long numberOfResultsGoogle) {
        this.pathDir = pathDir;
        this.word = word;
        this.top = top;
        this.numberOfResultsGoogle = numberOfResultsGoogle;
    }


    public Path getPathDir() {
        return pathDir;
    }

    public void setPathDir(Path pathDir) {
        this.pathDir = pathDir;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public long getTop() {
        return top;
    }

    public long getNumberOfResultsGoogle() {
        return numberOfResultsGoogle;
    }
}
