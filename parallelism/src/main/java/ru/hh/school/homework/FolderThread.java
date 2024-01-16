package ru.hh.school.homework;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class FolderThread extends Thread{

  private List<File> directory;
  List<File> folders = new ArrayList<>();
  private int countFolder;
  private Map<Path, List<Path>> folderToFilePaths = new HashMap<>();
  private final static Random random = new Random();

  public FolderThread(List<File> directory) {
    this.directory = directory;
    this.countFolder = 15;
  }

  @Override
  public void run() {
    scanDirectoryRecursive(directory);
    if (folders.size() > 0) {
      new FolderThread(folders).start();
    }
    folderToFilePaths.entrySet().forEach(item -> {
      new ScanFileThread(item.getKey(), item.getValue()).start();
    });
  }
  public void scanDirectoryRecursive(List<File> directories) {
    for (File directory: directories) {
      if (Objects.nonNull(directory.list())) {
        for (File file: directory.listFiles()) {
          if (file.isFile() && file.getName().endsWith(".java")) {
            Path path = file.toPath();
            Path folder = path.subpath(0, path.getNameCount() - 1);
            if (!folderToFilePaths.containsKey(folder)) {
              List<Path> paths = new ArrayList<>();
              paths.add(path);
              folderToFilePaths.put(folder, paths);
            } else {
              folderToFilePaths.get(folder).add(path);
            }
          } else if (file.isDirectory()) {
            folders.add(file);
            if(folders.size() == countFolder) {
              new FolderThread(new ArrayList<>(folders)).start();
              folders = new ArrayList<>();
            }
          }
        }
      }
    }
  }
}
