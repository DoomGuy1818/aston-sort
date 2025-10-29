import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import search.student.Student;
import sort.EvenSort;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class EvenStudentSortTest {

    @BeforeEach
    void resetStudentIdCounter() throws Exception {
        Field field = Student.class.getDeclaredField("nextId");
        field.setAccessible(true);
        field.set(null, new AtomicInteger(1));
    }

    @Test
    void testEmptyList() {
        List<Student> students = List.of();
        EvenSort sorter = new EvenSort(students);
        List<Student> result = sorter.sortEventStudents();

        assertTrue(result.isEmpty(), "Пустой список должен вернуть пустой результат");
    }

    @Test
    void testAllOddIds() {
        List<Student> students = new ArrayList<>();
        students.add(new Student.Builder().id(1).firstName("A").build());
        students.add(new Student.Builder().id(3).firstName("B").build());
        students.add(new Student.Builder().id(5).firstName("C").build());

        EvenSort sorter = new EvenSort(students);
        List<Student> result = sorter.sortEventStudents();

        assertEquals(List.of(1, 3, 5),
                result.stream().map(Student::getId).toList(),
                "Если все ID нечётные — порядок не должен меняться");
    }

    @Test
    void testAllEvenIds() {
        List<Student> students = new ArrayList<>();
        students.add(new Student.Builder().id(8).firstName("A").build());
        students.add(new Student.Builder().id(4).firstName("B").build());
        students.add(new Student.Builder().id(6).firstName("C").build());

        EvenSort sorter = new EvenSort(students);
        List<Student> result = sorter.sortEventStudents();

        assertEquals(List.of(4, 6, 8),
                result.stream().map(Student::getId).toList(),
                "Если все ID чётные — должен быть отсортирован весь список");
    }

    @Test
    void testMixedEvenAndOddIds() {
        List<Student> students = new ArrayList<>();
        students.add(new Student.Builder().id(5).firstName("A").build());
        students.add(new Student.Builder().id(2).firstName("B").build());
        students.add(new Student.Builder().id(4).firstName("C").build());
        students.add(new Student.Builder().id(1).firstName("D").build());

        EvenSort sorter = new EvenSort(students);
        List<Student> result = sorter.sortEventStudents();

        assertEquals(List.of(5, 2, 4, 1),
                result.stream().map(Student::getId).toList(),
                "Чётные должны быть отсортированы внутри своих позиций, нечётные остаются");
    }

    @Test
    void testEvenIdsOutOfOrder() {
        List<Student> students = new ArrayList<>();
        students.add(new Student.Builder().id(6).firstName("A").build());
        students.add(new Student.Builder().id(1).firstName("B").build());
        students.add(new Student.Builder().id(4).firstName("C").build());
        students.add(new Student.Builder().id(3).firstName("D").build());
        students.add(new Student.Builder().id(2).firstName("E").build());

        EvenSort sorter = new EvenSort(students);
        List<Student> result = sorter.sortEventStudents();

        assertEquals(List.of(2, 1, 4, 3, 6),
                result.stream().map(Student::getId).toList(),
                "Чётные 6,4,2 должны стать 2,4,6, сохраняя позиции нечётных");
    }

    @Test
    void testSingleEvenId() {
        List<Student> students = List.of(
                new Student.Builder().id(2).firstName("Solo").build()
        );

        EvenSort sorter = new EvenSort(students);
        List<Student> result = sorter.sortEventStudents();

        assertEquals(List.of(2),
                result.stream().map(Student::getId).toList(),
                "Один элемент — без изменений");
    }

    @Test
    void testBuilderNextIdIntegration() {
        List<Student> students = new ArrayList<>();
        students.add(new Student.Builder().nextId().firstName("A").build()); // id = 1
        students.add(new Student.Builder().nextId().firstName("B").build()); // id = 2
        students.add(new Student.Builder().nextId().firstName("C").build()); // id = 3
        students.add(new Student.Builder().nextId().firstName("D").build()); // id = 4

        EvenSort sorter = new EvenSort(students);
        List<Student> result = sorter.sortEventStudents();

        assertEquals(List.of(1, 2, 3, 4),
                result.stream().map(Student::getId).toList(),
                "Если ID уже упорядочены по nextId, результат не должен меняться");
    }

    @Test
    void testStabilityOddRemainInPlace() {
        List<Student> students = new ArrayList<>();
        students.add(new Student.Builder().id(9).firstName("A").build());
        students.add(new Student.Builder().id(2).firstName("B").build());
        students.add(new Student.Builder().id(7).firstName("C").build());
        students.add(new Student.Builder().id(4).firstName("D").build());

        EvenSort sorter = new EvenSort(students);
        List<Student> result = sorter.sortEventStudents();

        assertEquals("A", result.get(0).getFirstName());
        assertEquals("C", result.get(2).getFirstName());
    }
}

