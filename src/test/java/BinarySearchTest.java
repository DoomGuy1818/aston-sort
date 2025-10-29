package test.java;

import search.BinarySearch;
import search.student.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class BinarySearchTest {

    @Test
    void binarySearchExistStudentWithThreadE() {
        // Создаем список студентов
        List<Student> students = getStudentsWithSort();

        System.out.println("Исходный список студентов:");
        students.forEach(System.out::println);

        ExecutorService executor = Executors.newFixedThreadPool(4);

        int targetIndex = 2;
        // Поток 1: Поиск существующего студента
        executor.execute(() -> {
            Student target = students.get(targetIndex); // Берем студента из середины списка
            int index = BinarySearch.binarySearch(students, target);
            Assertions.assertEquals(targetIndex, index);
            System.out.println(Thread.currentThread().getName() +
                    ": Найден студент " + target.getFirstName() + " на позиции " + index);
        });
    }

    @Test
    void binarySearchNotExistStudentWithThreadE() {
        // Создаем список студентов
        List<Student> students = getStudentsWithSort();

        System.out.println("Исходный список студентов:");
        students.forEach(System.out::println);

        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Поток 2: Поиск несуществующего студента
        executor.execute(() -> {
            Student nonExistent = new Student("Non", "Existent");
            int index = BinarySearch.binarySearch(students, nonExistent);
            Assertions.assertEquals(-1, index);
            System.out.println(Thread.currentThread().getName() +
                    ": Поиск несуществующего студента, результат: " + index);
        });
    }

    List<Student> getStudentsWithSort() {
        // Создаем список студентов
        List<Student> students = Collections.synchronizedList(new ArrayList<>());

        // Добавляем студентов
        students.add(new Student("Ivan", "Ivanov"));
        students.add(new Student("Petr", "Petrov"));
        students.add(new Student("Anna", "Sidorova"));
        students.add(new Student("Maria", "Smirnova"));
        students.add(new Student("Alexey", "Kuznetsov"));

        // Сортируем по ID для бинарного поиска
        Collections.sort(students);

        return students;
    }
}
