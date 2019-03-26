package ru.hh.school.homework;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.nio.file.Path;

public class SearchThread implements Runnable {

    private String word;
    private Path dirPath;
    private Thread thread;

    SearchThread(String newWord, Path newDirPath) {
        word = newWord;
        dirPath = newDirPath;
        thread = new Thread(this, dirPath.toString().concat(word));
        thread.start();
    }

    @Override
    public void run() {
        try{
            String newLine = String.format("%s - %s - %d \n", dirPath.toString(), word, naiveSearch(word));
            System.out.print(newLine);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static long naiveSearch(String query) throws IOException {
        try{
        Document document = Jsoup //
                .connect("https://www.google.com/search?q=" + query) //
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.110 Safari/537.36 Viv/2.3.1440.48") //
                .get();
        Element divResultStats = document.select("div#resultStats").first();
        return Long.valueOf(divResultStats.text().replaceAll("[^0-9]", ""));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
