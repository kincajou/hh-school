package ru.hh.school.homework;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.reverseOrder;
import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

public class DirThread implements Runnable {

    private Thread thread;
    private Path dirPath;

    DirThread (Path newDirPath) {
        dirPath = newDirPath;
        thread = new Thread(this, dirPath.toString());
        thread.start();
    }

    @Override
    public void run() {
        javaFileIter(dirPath);
    }

    private void javaFileIter(Path dirPath) {
        try {
            Map <String, Long> wordMap = new HashMap<>();
            for (Path filePath : Files.newDirectoryStream(dirPath)) {
                if (Files.isDirectory(filePath)) {
                    new DirThread (filePath);
                }
                if (filePath.toString().endsWith(".java")) {
                    wordMap = StaticMethods.mapCombiner(wordMap, StaticMethods.naiveCount(filePath));
                }
            }

            wordMap = wordMap.entrySet()
            .stream()
            .sorted(comparingByValue(reverseOrder()))
            .limit(10)
            .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

            String outputString = outputStringAssemble(wordMap);
            System.out.print(outputString);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String outputStringAssemble (Map <String, Long> wordMap) {
        String outputString = "";
        for (String word : wordMap.keySet()) {
            new SearchThread(word, dirPath);
        }
        return outputString;
    }
}
