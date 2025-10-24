package search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import search.student.Student;

public class SolutionBinarySearchByStudent {

    public static void main1(String[] args) throws Exception {
        List<Student> students = Collections.synchronizedList(new ArrayList<>());

        Collections.sort(students);

        System.out.println("Исходный список студентов:");
        students.forEach(System.out::println);

        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Поток 1: Поиск существующего студента
        executor.execute(() -> {
            Student target = students.get(2); // Берем студента из середины списка
            int index = BinarySearch.binarySearch(students, target);
            System.out.println(Thread.currentThread().getName() +
                    ": Найден студент " + target.getFirstName() + " на позиции " + index);
        });

        // Поток 2: Поиск несуществующего студента
        executor.execute(() -> {
            Student nonExistent = new Student("Non", "Existent");
            int index = BinarySearch.binarySearch(students, nonExistent);
            System.out.println(Thread.currentThread().getName() +
                    ": Поиск несуществующего студента, результат: " + index);
        });
    }

    public static void main(String[] args) throws Exception {
        // Создаем список студентов
        List<Student> students = Collections.synchronizedList(new ArrayList<>());

        // Сортируем по ID для бинарного поиска
        Collections.sort(students);

        System.out.println("Исходный список студентов:");
        students.forEach(System.out::println);

        // Используем ForkJoinPool
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);

        try {
            forkJoinPool.submit(() -> {
                // Выполняем несколько поисковых задач параллельно
                List<Callable<Void>> tasks = Arrays.asList(
                        () -> {
                            Student target = students.get(2);
                            int index = BinarySearch.binarySearch(students, target);
                            System.out.println(Thread.currentThread().getName() +
                                    ": Найден студент " + target.getFirstName() + " на позиции " + index);
                            return null;
                        },
                        () -> {
                            Student nonExistent = new Student("Non", "Existent");
                            int index = BinarySearch.binarySearch(students, nonExistent);
                            System.out.println(Thread.currentThread().getName() +
                                    ": Поиск несуществующего студента, результат: " + index);
                            return null;
                        },
                        () -> {
                            Student target = students.get(0);
                            int index = BinarySearch.binarySearch(students, target);
                            System.out.println(Thread.currentThread().getName() +
                                    ": Найден первый студент " + target.getFirstName() + " на позиции " + index);
                            return null;
                        }
                );

                // Запускаем все задачи параллельно
                tasks.parallelStream().forEach(task -> {
                    try {
                        task.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }).get(); // Ждем завершения

        } finally {
            forkJoinPool.shutdown();
        }
    }
}
