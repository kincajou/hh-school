package ru.hh.school.homework;

import java.nio.file.Path;
import java.util.concurrent.ForkJoinPool;

public class Launcher {

    public static void main(String[] args){

        Path path = Path.of("C:\\1_Konstantin\\1_hh\\concurrency\\hh-school-1\\" +
                "parallelism\\src\\main\\java\\ru\\hh\\school");

        // рекурсивный характер задачи вынудил меня прибегнуть к ForkJoinPool
        ForkJoinPool pool =
                new ForkJoinPool(Runtime.getRuntime().availableProcessors());

        DirAction startDirAction = new DirAction(path);

        pool.invoke(startDirAction);

        // даже несмотря на это программа не завершается
        // и после проведения всех требуемых вычислений:
        // видимо, где-то не сджойнился тред
        pool.shutdownNow();
    }
}