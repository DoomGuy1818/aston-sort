package utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import search.student.Student;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FileManager {

    // Загрузка из текстового файла
    public static void loadFromTextFile(String path, List<Student> students) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            int currentMaxId = students.stream()
                    .mapToInt(Student::getId)
                    .max()
                    .orElse(0);

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 2) {
                    Student s = new Student(parts[0], parts[1]);
                    setStudentId(s, ++currentMaxId);
                    students.add(s);
                }
            }
        }
    }

    // Загрузка из JSON файла
    public static void loadFromJsonFile(String path, List<Student> students) throws IOException {
        try (Reader reader = new FileReader(path)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>)
                            (json, type, context) -> LocalDateTime.parse(json.getAsString(), java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME))
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

                // обновляем nextId в классе Student
                setNextId(currentMaxId + loaded.size() + 1);
            }
        }
    }

    // Вспомогательный метод: установить ID студенту через reflection
    private static void setStudentId(Student s, int id) {
        try {
            java.lang.reflect.Field idField = Student.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.setInt(s, id);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Ошибка установки ID студенту", e);
        }
    }

    // Вспомогательный метод: установить nextId
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

