package search.student;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Потока независимый class Student
 */
public class Student implements Comparable<Student> {
    private static AtomicInteger nextId = new AtomicInteger(1);
    private int id;
    private String firstName;
    private String lastName;
    private LocalDateTime birthDay;

    public Student(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = nextId.getAndIncrement();
    }

    public Student(String firstName, String lastName, LocalDateTime birthDay) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.id = nextId.getAndIncrement();
    }

    public String getFirstName() {
        return firstName;
    }

    public synchronized void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public synchronized void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getBirthDay() {
        return birthDay;
    }

    public synchronized void setBirthDay(LocalDateTime birthDay) {
        this.birthDay = birthDay;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public int compareTo(Student o) {
        return Integer.compare(id, o.id);
    }
}
