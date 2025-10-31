package utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import search.student.Student;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FileManager {
    public static void loadFromJsonFile(String path, List<Student> students) throws IOException {
        try (Reader reader = new FileReader(path)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, context) -> {
                        String value = json.getAsString().trim();
                        DateTimeFormatter[] formatters = new DateTimeFormatter[]{
                                DateTimeFormatter.ISO_LOCAL_DATE,                 // 2002-05-03
                                DateTimeFormatter.ofPattern("dd-MM-yyyy"),        // 05-03-2002
                                DateTimeFormatter.ofPattern("dd/MM/yyyy"),        // 05/03/2002
                                DateTimeFormatter.ofPattern("yyyy/MM/dd")         // 2002/03/05
                        };
                        for (DateTimeFormatter f : formatters) {
                            try {
                                return LocalDate.parse(value, f);
                            } catch (DateTimeParseException ignored) {
                            }
                        }
                        System.out.println("Некорректный формат даты: " + value + ". В поле birthDay будет установлено значение null.");
                        return null;
                    })
                    .create();
            Type listType = new TypeToken<List<Student>>() {}.getType();
            List<Student> loaded = gson.fromJson(reader, listType);
            if (loaded != null && !loaded.isEmpty()) {
                int currentMaxId = students.stream()
                        .mapToInt(Student::getId)
                        .max()
                        .orElse(0);
                for (int i = 0; i < loaded.size(); i++) {
                    Student s = loaded.get(i);
                    setStudentId(s, currentMaxId + i + 1);
                    students.add(s);
                }
                setNextId(currentMaxId + loaded.size() + 1);
            }
        }
    }
    // метод для установки ID студенту через reflection
    private static void setStudentId(Student s, int id) {
        try {
            java.lang.reflect.Field idField = Student.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.setInt(s, id);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Ошибка установки ID объекту", e);
        }
    }
    // метод для установки nextId
    private static void setNextId(int next) {
        try {
            java.lang.reflect.Field nextIdField = Student.class.getDeclaredField("nextId");
            nextIdField.setAccessible(true);
            AtomicInteger nextId = (AtomicInteger) nextIdField.get(null);
            nextId.set(next);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Ошибка установки nextId", e);
        }
    }
}