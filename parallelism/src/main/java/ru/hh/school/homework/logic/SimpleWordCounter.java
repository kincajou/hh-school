package ru.hh.school.homework.logic;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Collections.reverseOrder;
import static java.util.Map.Entry.comparingByValue;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

public class SimpleWordCounter {
    protected static Map<String, Long> naiveCount(Path path) {
        try {
            return Files.lines(path)
                    .flatMap(line -> Stream.of(line.split("[^a-zA-Z0-9]")))
                    .filter(word -> word.length() > 3)
                    .collect(groupingBy(identity(), counting()))
                    .entrySet()
                    .stream()
                    .sorted(comparingByValue(reverseOrder()))
                    .limit(10)
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static long naiveSearch(String query) {
        Document document = null;
        try {
            document = Jsoup //
                    .connect("https://www.google.com/search?q=" + query) //
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.116 Safari/537.36") //
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert document != null;
        Element divResultStats = document.select("div#slim_appbar").first();

        if (divResultStats == null) {
            return 0;
        } else {
            String text = divResultStats.text();
            String resultsPart = text.substring(0, text.indexOf('('));
            return Long.parseLong(resultsPart.replaceAll("[^0-9]", ""));
        }
    }

}
