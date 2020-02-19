package ru.hh.school.homework;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

public class DirAction extends RecursiveAction {

    /*
    Класс, описывающий итерирование по содержимому файла. Если натыкаемся на директорию,
    рекурсивно вызывается новый экземпляр DirAction
     */

    private static final Logger LOGGER = getLogger(Launcher.class);

    private Path dirPath;

    // мапа для "топовых" слов "припрятана" в private-аттрибут для потокобезопасности
    private Map <String, Long> wordMap = new HashMap<>();

    DirAction(Path newDirPath) {
        dirPath = newDirPath;
    }

    @Override
    public void compute() {
        javaFileIter(dirPath);
    }

    private void javaFileIter(Path dirPath) {
        try {
            for (Path filePath : Files.newDirectoryStream(dirPath)) {
                if (Files.isDirectory(filePath)) {
                    DirAction nestedDirAction = new DirAction(filePath);
                    nestedDirAction.fork();
                }
                if (!Files.isDirectory(filePath) && filePath.toString().endsWith(".java")) {
                    wordMap = StaticMethods.mapCombiner(wordMap,
                            StaticMethods.naiveCount(filePath));
                }
            }

            // из мапы "топовых" слов, собранных по отдельным файлам, собирает top-10
            // для данной директории dirPath
            wordMap = StaticMethods.mapTop(wordMap);

            this.outputStringAssemble();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // даже несмотря на этот join какой-то тред не закрывается
            // даже после проведения всех требуемых вычислений :(
            this.join();
        }
    }

    private void outputStringAssemble () {
        SearchCall searchCall = new SearchCall(dirPath);
        try {
            String outputString = "";
            ArrayList<Future<String>> futureList = new ArrayList<>();
            for (String word : wordMap.keySet()) {
                Future<String> future = searchCall.getNewString(word);
                futureList.add(future);
            }
            for (Future<String> future : futureList) {
                outputString = outputString.concat(future.get());
            }
            LOGGER.debug(outputString);
        } catch (InterruptedException | ExecutionException e){
            throw new RuntimeException();
        } finally {
            searchCall.shutdownExecutor();
        }
    }
}
