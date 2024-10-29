package deque;

import java.util.Iterator;

public interface Deque<T> {

    public void addFirst(T t);

    public void addLast(T t);

    public int size();

    public void printDeque();

    public T removeFirst();

    public T removeLast();

    public Iterator<T> iterator();

    public boolean equals(Object o);

    public T get(int index);

    default public boolean isEmpty() {
        if (this.size() == 0) {
            return true;
        }
        return false;
    }
}
