package sort;

import java.util.List;

public class InsertionSort {

    public static synchronized <T extends Comparable<? super T>> List<T> sort(List<T> list) {
        int n = list.size();

        for (int i = 1; i < n; i++) {
            T tmp = list.get(i);
            int j = i - 1;

            while (j >= 0 && list.get(j).compareTo(tmp) > 0) {
                list.set(j + 1, list.get(j));
                j--;
            }

            list.set(j + 1, tmp);
        }

        return list;
    }
}
