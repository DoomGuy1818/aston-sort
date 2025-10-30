
import search.BinarySearch;
import search.student.Student;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import sort.EvenSort;
import utils.FileManager;

public class Application {
    private static final Scanner sc = new Scanner(System.in);
    private static List<Student> students = new ArrayList<>();

    public static void main(String[] args) {
        boolean exitFlag = false;

        System.out.println("Добро пожаловать в приложение для сортировки кастомных классов.");
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
//                case 4 -> ;
                case 6 -> binarySearch();
                case 5 -> sortEvenObjects();
                case 7 -> printStudents();
                case 8 -> clearStudents();
                case 9 -> {
                    System.out.println("Вы уверены, что хотите выйти? " +
                            "\n1. Да;" +
                            "\n2. Нет.");
                    int confirm = inputInt("Выберите действие: ");
                    if (confirm == 1) exitFlag = true;
                }
                default -> System.out.println("Неизвестная команда. Пожалуйста, повторите попытку");
            }
        }
        System.out.println("Программа завершена.");
    }

    private static void loadFromFile() {
        System.out.print("Введите путь к файлу: ");
        String path = sc.next();
        try {
            if (path.toLowerCase().endsWith(".json")) {
                FileManager.loadFromJsonFile(path, students);
                System.out.println("Данные успешно загружены из JSON. Количество объектов: " + students.size());
            } else {
                FileManager.loadFromTextFile(path, students);
                System.out.println("Данные успешно загружены из текстового файла. Количество объектов: " + students.size());
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
                        .mapToObj(i -> new Student(
                                firstNames[random.nextInt(firstNames.length)],
                                lastNames[random.nextInt(lastNames.length)],
                                LocalDateTime.now().minusYears(random.nextInt(10) + 18)
                        ))
                        .toList()
        );

        System.out.println("Создано " + n + " случайных объектов.");
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

                            return new Student(first, last, LocalDateTime.now());
                        })
                        .toList()
        );

        System.out.println("Создано " + n + " объектов.");
        printStudents();
    }
    private static void printStudents() {
        if (students.isEmpty()) {
            System.out.println("Список пуст.");
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
                System.out.println("Ошибка ввода. Пожалуйста, повторите попытку.");
            }
        }
    }
    private static void clearStudents() {
        if (students.isEmpty()) {
            System.out.println("Список уже пуст.");
            return;
        }
        System.out.println("Вы уверены, что хотите очистить список объектов? " +
                "\n1. Да;" +
                "\n2. Нет.");
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
            System.out.println("Список объектов успешно очищен.");
        } else {
            System.out.println("Очистка отменена.");
        }
    }
    private static void sortEvenObjects() {
        if (students.isEmpty()) {
            System.out.println("Список пуст. Пожалуйста, добавьте объекты для сортировки.");
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
            System.out.println("Нет нужного студента");
        }

        System.out.println("Введите имя:");
        String name = sc.next();
        System.out.println("Введите Фамилию");
        String lastname = sc.next();
        System.out.println("Введите ID студента");
        int id = Integer.parseInt(sc.next());

        Student.Builder sBuilder = new Student.Builder();
        LocalDateTime birthDay = LocalDateTime.of(2004, 5, 15, 0, 0);
        sBuilder.id(id).firstName(name).lastName(lastname).birthDay(birthDay);
        Student target = new Student(sBuilder);
        int index = BinarySearch.binarySearch(students, target);

        System.out.println("Ваш студент это: " + students.get(index));
    }
}