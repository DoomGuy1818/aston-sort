import sort.InsertionSort;
import search.student.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InsertionSortTest {
    
    @Test
    void insertionSortTest() {

        List<Student> students = this.getStudents();
        Student a = students.get(2);
        Student b = students.get(0);

        students.set(0, a);
        students.set(2, b);

        System.out.println(students);

        ExecutorService executor = Executors.newFixedThreadPool(4);

        executor.execute(() -> {
            List<Student> sortList = InsertionSort.sort(students);
            Assertions.assertEquals(sortList, students);
        });
        List<Student> sortList = InsertionSort.sort(students);

        System.out.println(sortList);
    }

    List<Student> getStudents() {
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
