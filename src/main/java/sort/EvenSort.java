package sort;

import search.student.Student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class EvenSort {
    public List<Student> students;

    public EvenSort(List<Student> students) {
        this.students = students;
    }

    public List<Student> sortEventStudents() {
        List<Student> result = new ArrayList<>(students);

        List<Student> evenStudents = new ArrayList<>();
        for (Student s : result) {
            if (s.getId() % 2 == 0) {
                evenStudents.add(s);
            }
        }

        evenStudents.sort(Comparator.comparingInt(Student::getId));

        Iterator<Student> sortedEvenIt = evenStudents.iterator();
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getId() % 2 == 0) {
                result.set(i, sortedEvenIt.next());
            }
        }

        return result;
    }
}
