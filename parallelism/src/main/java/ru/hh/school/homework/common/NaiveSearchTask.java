package ru.hh.school.homework.common;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

public class NaiveSearchTask implements Runnable {

    private String query;

    private Path directoryPath;

    public NaiveSearchTask(String query, Path directoryPath) {
        this.query = query;
        this.directoryPath = directoryPath;
    }

    public void run() {
        try {
            printGoogleCount();
        } catch (IOException e) {
            System.out.printf("%s - %s - %s\r\n", directoryPath, query, "It is impossible to count");
        }
    }

    private void printGoogleCount() throws IOException {
        System.out.printf("%s - %s - %d\r\n", directoryPath, query, naiveSearchMock(query));
    }

    private long naiveSearch(String query) throws IOException {
        Document document = Jsoup //
                .connect("https://www.google.com/search?q=" + query) //
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.116 Safari/537.36") //
                .get();

        Element divResultStats = document.select("div#result-stats").first(); //div#slim_appbar div#result-stats
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