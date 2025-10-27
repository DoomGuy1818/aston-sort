package search.student;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class OutputHandler {
    private static final String FILE_NAME = "students_output.txt";

    public static void writeSortedStudents(List<Student> students) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write("Отсортированные студенты:");
            writer.newLine();
            for (Student student : students) {
                writer.write(student.toString());
                writer.newLine();
            }
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Ошибка записи отсортированных студентов: " + e.getMessage());
        }
    }

    public static void writeSearchResult(int index, Student target) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            if (index != -1) {
                writer.write("Студент найден на позиции: " + index);
            } else {
                writer.write("Студент не найден.");
            }
            writer.newLine();
            writer.write("Искомый студент: " + target);
            writer.newLine();
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Ошибка записи результата поиска: " + e.getMessage());
        }
    }
}
