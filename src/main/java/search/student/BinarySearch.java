package search.student;

import java.util.List;

public class BinarySearch<T extends Comparable <T>> {
    public int search(List<T> list, T target) {
        int left = 0;
        int right = list.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            int cmp = list.get(mid).compareTo(target);

            if (cmp == 0) {
                return mid; // найденный элемент
            } else if (cmp < 0) {
                left = mid + 1; // поиск в правой части
            } else {
                right = mid - 1; // поиск в левой части
            }
        }
        return -1; // элемент не найден
    }
}