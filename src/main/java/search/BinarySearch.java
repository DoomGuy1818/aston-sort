package search;

import java.util.List;

public class BinarySearch {

    /**
     * Реализация потока независимого бинарного поиска
     * @param list
     * @param target
     * @param <T>
     * @return
     */
    public static synchronized <T extends Comparable<? super T>> int binarySearch(List<T> list, T target) {
        int l = 0, r = list.size() - 1;

        while (l <= r) {
            int mid = (l + r) / 2;
            T midValue = list.get(mid);
            int compMidToTarget = midValue.compareTo(target);

            if (compMidToTarget < 0) {
                l = mid + 1;
            } else if (compMidToTarget > 0) {
                r = mid - 1;
            } else {
                return mid;
            }
        }

        return -1;
    }
}
