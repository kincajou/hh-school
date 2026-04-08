package ru.hh.school.homework;

import java.io.File;
import java.util.List;

public class Main {
  public static void main(String[] args) throws InterruptedException {
    //Path path = Path.of("d:\\projects\\work\\hh-school\\parallelism\\src\\main\\java\\ru\\hh\\school\\parallelism\\Runner.java");
    String string = "C:\\Users\\nedomets.aleksandr\\IdeaProjects";
    File file = new File(string);
    if (file.isDirectory()) new FolderThread(List.of(file)).start();
  }
}
