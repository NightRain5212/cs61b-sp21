package deque;

import java.util.Iterator;

public interface Deque<T> {

    void addFirst(T t);

    void addLast(T t);

    int size();

    void printDeque();

    T removeFirst();

    T removeLast();

    Iterator<T> iterator();

    boolean equals(Object o);

    T get(int index);

    default boolean isEmpty() {
        if (this.size() == 0) {
            return true;
        }
        return false;
    }
}
