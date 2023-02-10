package ru.hh.school.homework;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class GoogleThread extends Thread {

  private Path folder;
  private List<String> worlds;

  public GoogleThread(Path folder, List<String> worlds) {
    this.folder = folder;
    this.worlds = worlds;
  }
  @Override
  public void run() {
    worlds.forEach(this::naiveSearch);
  }

  private void naiveSearch(String query) {
    Document document;
    try {
      document = Jsoup
          .connect("https://www.google.com/search?q=" + query)
          .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36")
          .get();
    } catch (IOException e) {
      System.out.println(getDescriptionResult(query, "GOOGLE BAN"));
      return;
    }
    Element divResultStats = document.select("div#slim_appbar").first();
    String text = divResultStats.text();
    if ("".equals(text)) {
      System.out.println(getDescriptionResult(query, "NO GOOGLE RESULT"));
      return;
    }
    String result = text.substring(0, text.indexOf('(')).replaceAll("\\D", "");
    System.out.println(getDescriptionResult(query, result));
  }

  private String getDescriptionResult(String word, String googleResult) {
    return String.format("Folder: %s;\n Word: %s; Approximate number of results from Google: %s", folder, word, googleResult);
  }

}
