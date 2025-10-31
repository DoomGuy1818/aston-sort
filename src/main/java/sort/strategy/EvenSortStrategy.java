package sort.strategy;

import search.student.Student;
import sort.EvenSort;

import java.util.List;

public class EvenSortStrategy implements SortStrategy<Student> {

    @Override
    public List<Student> sort(List<Student> list) {
        EvenSort evenSort = new EvenSort(list);
        return evenSort.sortEventStudents();
    }
}