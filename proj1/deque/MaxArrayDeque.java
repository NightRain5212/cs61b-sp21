package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    //私有比较器属性
    private Comparator<T> cpt;

    public MaxArrayDeque(Comparator<T> c) {
        cpt = c;
    }

    public T max() {
        if (this.isEmpty()) {
            return null;
        }
        T themax = this.get(0);
        for (T i : this) {
            if (cpt.compare(i, themax) > 0) {
                themax = i;
            }
        }
        return themax;
    }

    public T max(Comparator<T> c) {
        if (this.isEmpty()) {
            return null;
        }
        T themax = this.get(0);
        for (T i : this) {
            if (c.compare(i, themax) > 0) {
                themax = i;
            }
        }
        return themax;
    }

}
