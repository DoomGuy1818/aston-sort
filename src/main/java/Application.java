import search.BinarySearch;
import search.student.Student;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import sort.EvenSort;
import sort.InsertionSort;
import utils.FileManager;

public class Application {
    private static final Scanner sc = new Scanner(System.in);
    private static List<Student> students = new ArrayList<>();
    public static void main(String[] args) {
        boolean exitFlag = false;
        System.out.println("Добро пожаловать в приложение для сортировки кастомных классов!");
        while (!exitFlag) {
            System.out.println("\n1. Загрузить данные из файла");
            System.out.println("2. Заполнить данные случайным образом");
            System.out.println("3. Ввести данные вручную");
            System.out.println("4. Отсортировать объекты");
            System.out.println("5. Отсортировать объекты с чётными ID");
            System.out.println("6. Найти объект");
            System.out.println("7. Вывести все объекты");
            System.out.println("8. Очистить список объектов");
            System.out.println("9. Выйти");
            int userVariant = inputInt("Выберите действие: ");
            switch (userVariant) {
                case 1 -> loadFromFile();
                case 2 -> fillRandom();
                case 3 -> fillManually();
                case 4 -> sortObjects();
                case 5 -> sortEvenObjects();
                case 6 -> binarySearch();
                case 7 -> printStudents();
                case 8 -> clearStudents();
                case 9 -> {
                    System.out.println("Вы уверены, что хотите выйти? " +
                            "\n1. Да" +
                            "\n2. Нет");
                    int confirm = inputInt("Выберите действие: ");
                    if (confirm == 1) exitFlag = true;
                }
                default -> System.out.println("Неизвестная команда. Пожалуйста, повторите попытку");
            }
        }
        System.out.println("Программа завершена");
    }
    public static void sortObjects() {
        if (students == null || students.isEmpty()) {
            System.out.println("Список пуст. Пожалуйста, добавьте объекты для сортировки");
            return;
        }
        Class<?> classOfObjects = students.get(0).getClass();
//        System.out.println("Класс объектов: " + classOfObjects.getSimpleName());
        List<Field> fields = Arrays.stream(classOfObjects.getDeclaredFields())
                .filter(f -> !Modifier.isStatic(f.getModifiers()))
                .filter(f -> Comparable.class.isAssignableFrom(f.getType())).toList();
        if (fields.isEmpty()) {
            System.out.println("Не найдены поля, которые должны реализовывать Comparable и подходящих для сортировки");
            return;
        }
        System.out.println("Выберите поле для сортировки: ");
        for (int i = 0; i < fields.size(); i++) {
            System.out.println((i + 1) + ". " + fields.get(i).getName());
        }
        int choice = inputInt("Введите номер поля: ");
        if (choice < 1 || choice > fields.size()) {
            System.out.println("Некорректный выбор. Пожалуйста, повторите попытку");
            return;
        }
        Field field = fields.get(choice - 1);
        field.setAccessible(true);
        System.out.println("""
        Выберите порядок сортировки:
        1. По возрастанию
        2. По убыванию
        """);
        int direction = inputInt("Введите номер: ");
        boolean descending = direction == 2;
        try {
            class StudentWrapper implements Comparable<StudentWrapper> {
                Student student;
                StudentWrapper(Student s) { this.student = s; }
                @Override
                public int compareTo(StudentWrapper o) {
                    try {
                        Comparable v1 = (Comparable) field.get(this.student);
                        Comparable v2 = (Comparable) field.get(o.student);
                        return v1.compareTo(v2);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            List<StudentWrapper> wrapperList = students.stream()
                    .map(StudentWrapper::new)
                    .collect(Collectors.toList());
            InsertionSort.sort(wrapperList);
            if (descending) Collections.reverse(wrapperList);
            students.clear();
            for (StudentWrapper w : wrapperList) {
                students.add(w.student);
            }

            printStudents();
            System.out.println("Список успешно отсортирован по полю: " + field.getName() +
                    (descending ? " по убыванию" : " по возрастанию"));
        } catch (ClassCastException e) {
            System.out.println("Невозможно сравнить значения поля - несовместимые типы");
        } catch (RuntimeException e) {
            System.out.println("Ошибка при сортировке: выбранное поле " + field + " содержит значение null" );
        }
    }
    private static void loadFromFile() {
        System.out.print("Введите путь к файлу: ");
        String path = sc.next();
        try {
            if (path.toLowerCase().endsWith(".json")) {
                FileManager.loadFromJsonFile(path, students);
                System.out.println("Данные успешно загружены из JSON. Количество объектов: " + students.size());
            }
            printStudents();
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        }
    }
    private static void fillRandom() {
        int n = inputInt("Введите количество объектов: ");
        String[] firstNames = {"Виктория", "Кристина", "Ахтем", "Евгений", "Андрей"};
        String[] lastNames = {"Иванченко", "Петров", "Сидоренко", "Кузнецов", "Смирнов"};
        Random random = new Random();

        students.addAll(
                IntStream.range(0, n)
                        .mapToObj(i -> {
                            int age = random.nextInt(10) + 18; // возраст 18–27
                            int year = LocalDate.now().getYear() - age;
                            int month = random.nextInt(12) + 1;
                            int day = random.nextInt(
                                    Month.of(month).length(Year.isLeap(year))
                            ) + 1; // корректное число дней в месяце
                            LocalDate birthDate = LocalDate.of(year, month, day);
                            return new Student(
                                    firstNames[random.nextInt(firstNames.length)],
                                    lastNames[random.nextInt(lastNames.length)],
                                    birthDate
                            );
                        })
                        .toList()
        );
        System.out.println("Создано " + n + " случайных объектов");
        printStudents();
    }
    private static void fillManually() {
        int n = inputInt("Введите количество объектов: ");
        students.addAll(
                IntStream.range(0, n)
                        .mapToObj(i -> {
                            System.out.print("Введите имя объекта №" + (i + 1) + ": ");
                            String first = sc.next();

                            System.out.print("Введите фамилию объекта №" + (i + 1) + ": ");
                            String last = sc.next();

                            LocalDate birthDate = null;
                            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE; // формат YYYY-MM-DD

                            while (birthDate == null) {
                                System.out.print("Введите дату рождения (в формате YYYY-MM-DD) объекта №" + (i + 1) + ": ");
                                String input = sc.next();
                                try {
                                    birthDate = LocalDate.parse(input, formatter);
                                    if (birthDate.isAfter(LocalDate.now())) {
                                        System.out.println("Ошибка даты: дата рождения не может быть в будущем. Пожалуйста, повторите попытку");
                                        birthDate = null;
                                    }
                                } catch (DateTimeParseException e) {
                                    System.out.println("Ошибка формата: пожалуйста, введите дату в виде YYYY-MM-DD (например, 2003-12-25)");
                                }
                            }
                            return new Student(first, last, birthDate);
                        })
                        .toList()
        );
        System.out.println("Создано " + n + " объектов");
        printStudents();
    }
    private static void printStudents() {
        if (students.isEmpty()) {
            System.out.println("Список пуст");
        } else {
            students.forEach(System.out::println);
        }
    }
    private static int inputInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.next());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка ввода. Пожалуйста, повторите попытку");
            }
        }
    }
    private static void clearStudents() {
        if (students.isEmpty()) {
            System.out.println("Список уже пуст");
            return;
        }
        System.out.println("Вы уверены, что хотите очистить список объектов? " +
                "\n1. Да" +
                "\n2. Нет");
        int confirm = inputInt("Выберите действие: ");
        if (confirm == 1) {
            students.clear();
            try {
                java.lang.reflect.Field nextIdField = Student.class.getDeclaredField("nextId");
                nextIdField.setAccessible(true);
                AtomicInteger nextId = (AtomicInteger) nextIdField.get(null);
                nextId.set(1);
            } catch (ReflectiveOperationException e) {
                System.out.println("Ошибка при сбросе ID: " + e.getMessage());
            }
            System.out.println("Список объектов успешно очищен");
        } else {
            System.out.println("Очистка отменена");
        }
    }
    private static void sortEvenObjects() {
        if (students.isEmpty()) {
            System.out.println("Список пуст. Пожалуйста, добавьте объекты для сортировки");
            return;
        }
        EvenSort evenSorter = new EvenSort(students);
        List<Student> sorted = evenSorter.sortEventStudents();
        System.out.println("Список объектов после сортировки по чётным ID:");
        sorted.forEach(System.out::println);
        students = sorted;
    }
    private static void binarySearch() {
        if (students.isEmpty()) {
            System.out.println("Список пуст. Пожалуйста, добавьте объекты для поиска");
        }
        System.out.println("Введите ID объекта:");
        int id = Integer.parseInt(sc.next());
        Student.Builder sBuilder = new Student.Builder();
        sBuilder.id(id);
        Student target = new Student(sBuilder);
        int index = BinarySearch.binarySearch(students, target);
        if (index >= 0 && index < students.size()) {
            System.out.println("Найденный объект: " + students.get(index));
        } else {
            System.out.println("Объект с заданным ID = " + id + " не найден");
        }
    }
}