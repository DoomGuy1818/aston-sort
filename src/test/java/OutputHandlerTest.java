

import org.junit.jupiter.api.Test;
import search.student.OutputHandler;
import search.student.Student;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class OutputHandlerTest {

    // Имя файла, в который пишет OutputHandler
    private static final String FILE_NAME = "students_output.txt";

    @Test
    void testWriteSortedStudents() throws IOException {
        // Подготовка
        List<Student> students = new ArrayList<>();
        LocalDate birthDayIvan = LocalDate.of(2004, 5, 15);
        students.add(new Student("Иван", "Иванов", birthDayIvan));

        LocalDate birthDayAnna = LocalDate.of(2003, 10, 20 );
        students.add(new Student("Анна", "Смирнова", birthDayAnna));

        // Удаление старого файла перед тестом
        File file = new File(FILE_NAME);
        if (file.exists()) {
            file.delete();
        }

        // Вызов метода
        OutputHandler.writeSortedStudents(students);

        // Проверка: файл создан?
        assertTrue(file.exists());

        // Проверка содержимого файла
        List<String> lines = Files.readAllLines(file.toPath());
        assertEquals("Отсортированные студенты:", lines.get(0));
        assertTrue(lines.get(1).contains("Иван"));
        assertTrue(lines.get(2).contains("Анна"));
    }

    @Test
    void testWriteSearchResult_StudentFound() throws IOException {
        Student target = new Student("Иван", "Иванов", LocalDate.of(2004, 5, 15));
        int index = 0;

        // Убедимся, что файл существует или создаём его
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            file.createNewFile();
        }

        // Вызов метода
        OutputHandler.writeSearchResult(index, target);

        // Проверка содержимого
        List<String> lines = Files.readAllLines(file.toPath());
        String lastLine = lines.get(lines.size() - 1);
        assertTrue(lastLine.contains("Студент найден на позиции: 0"));
        assertTrue(lines.get(lines.size() - 2).contains("Искомый студент: Student"));
    }

    @Test
    void testWriteSearchResult_StudentNotFound() throws IOException {
        Student target = new Student("Иван", "Иванов", LocalDate.of(2004, 5, 15));
        int index = -1;

        // Убедимся, что файл существует
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            file.createNewFile();
        }

        // Вызов метода
        OutputHandler.writeSearchResult(index, target);

        // Проверка содержимого
        List<String> lines = Files.readAllLines(file.toPath());
        String lastLine = lines.get(lines.size() - 1);
        assertTrue(lastLine.contains("Студент не найден."));
        assertTrue(lines.get(lines.size() - 2).contains("Искомый студент: Student"));
    }
}