package ru.hh.school.homework;

import java.io.IOException;
import java.nio.file.*;

public class Launcher {

    public static void main(String[] args) throws IOException {


        // Написать код, который, как можно более параллельно:
        // - по заданному пути найдет все "*.java" файлы
        // - для каждого файла вычислит 10 самых популярных слов (см. #naiveCount())
        // - соберет top 10 для каждой папки в которой есть хотя-бы один java файл
        // - для каждого слова сходит в гугл и вернет количество результатов по нему (см. #naiveSearch())
        // - распечатает в консоль результаты в виде:
        // <папка1> - <слово #1> - <кол-во результатов в гугле>
        // <папка1> - <слово #2> - <кол-во результатов в гугле>
        // ...
        // <папка1> - <слово #10> - <кол-во результатов в гугле>
        // <папка2> - <слово #1> - <кол-во результатов в гугле>
        // <папка2> - <слово #2> - <кол-во результатов в гугле>
        // ...
        // <папка2> - <слово #10> - <кол-во результатов в гугле>
        // ...
        //
        // Порядок результатов в консоли не обязательный.
        // При желании naiveSearch и naiveCount можно оптимизировать.
        String dirPath = args.length > 0 ? args[0] : "/home/artur/hh/hh-school";
        String extensions = args.length > 1 ? args[1] : "java";

        Path path = Path.of(dirPath);
        SearchDirectoryService searchDirectoryService = new SearchDirectoryService(path, extensions);
        StatisticService statisticService = new StatisticService(searchDirectoryService);
        statisticService.calculateStatistic();
        statisticService.shutDown();
    }

}
