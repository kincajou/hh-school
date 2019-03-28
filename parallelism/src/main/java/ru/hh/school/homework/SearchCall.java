package ru.hh.school.homework;

import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SearchCall {

    /*
    *
    Класс, реализующий асинхронную отсылку запросов на гугл.

    Не придумал, как засунуть эти задачи в общий ForkJoinPool,
    поэтому для каждой директории будет создаваться свой класс SearchCall,
    в котором будет скромный тредпул на пять потоков.

    Таким образом, наибольшее количество потоков, которое может получиться:
    20 = 5 (SearchCall) * 4 (на главном ForkJoinPool)
     *
     */

    private Path dirPath;
    private ExecutorService executor = Executors.newFixedThreadPool(5);

    SearchCall(Path newDirPath) {
        dirPath = newDirPath;
    }

    public Future <String> getNewString (String word) {
        return executor.submit(() -> {
            return String.format("%s - %s - %d \n",
                    dirPath.toString(), word, StaticMethods.naiveSearch(word));
        });
    }

    // Конечно, после выполнения запросов весь этот класс будет убит garbege collector'ом,
    // но все же я решил предусмотреть остановку Executor-сервиса
    public void shutdownExecutor(){
        executor.shutdownNow();
    }
}
