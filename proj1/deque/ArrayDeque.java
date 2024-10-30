package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T>, Deque<T> {
    //跟踪大小
    private int size;
    //存储数据的数组
    private T[] items;
    //数组容量
    private int capacity = 8;


    //记录首元素的位置
    private int firstIndex;
    //末端元素位置
    private int lastIndex;

    //构造函数
    public ArrayDeque() {
        size = 0;
        items = (T[]) new Object[capacity];
        firstIndex = 0;
        lastIndex = capacity - 1;
    }

    //addFirst
    @Override
    public void addFirst(T t) {
        if (size == capacity) {
            resize(capacity * 2);
        }
        //更新指向，防止负数
        firstIndex = (firstIndex - 1 + capacity) % capacity;
        items[firstIndex] = t;
        //更新大小
        size += 1;
    }

    //addLast
    @Override
    public void addLast(T t) {
        if (size == capacity) {
            resize(capacity * 2);
        }
        //更新指向
        lastIndex = (lastIndex + 1) % capacity;
        items[lastIndex] = t;
        //更新大小
        size += 1;
    }

    //removeFirst
    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        //记录返回值并清空
        T returnitem = items[firstIndex];
        items[firstIndex] = null;
        //更新大小
        size -= 1;
        //更新指向
        firstIndex = (firstIndex + 1 + capacity) % capacity;
        //调整数组大小
        if ((capacity >= 16) && size < capacity / 4) {
            resize(capacity / 4);
        }
        return returnitem;
    }

    //removeLast
    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T returnitem = items[lastIndex];
        items[lastIndex] = null;
        //调整数组大小

        //更新指向
        lastIndex = (lastIndex - 1 + capacity) % capacity;
        //更新大小
        size -= 1;
        if ((capacity >= 16) && size < capacity / 4) {
            resize(capacity / 4);
        }
        return returnitem;
    }

    //size
    @Override
    public int size() {
        return size;
    }

    //get
    @Override
    public T get(int i) {
        if (i < 0 || i >= size) {
            return null;
        }
        return items[(firstIndex + i) % capacity];
    }

//    //isEmpty
//    @Override
//    public boolean isEmpty(){
//        if(size == 0){
//            return true;
//        }
//        return false;
//    }

    //printDeque
    @Override
    public void printDeque() {
        for (T i : this) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    @Override
    public Iterator<T> iterator() {
        return new DequeIterator();
    }

    //resize扩容
    private void resize(int newcapacity) {
        T[] newitems = (T[]) new Object[newcapacity];
        //复制队列元素
        int j = 0;
        for (int i = firstIndex; j < size; i = (i + 1) % capacity, j++) {
            newitems[j] = items[i];
        }
        //更新
        capacity = newcapacity;
        firstIndex = 0;
        lastIndex = size - 1;
        items = newitems;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque otherdeque = (Deque) o;
        if (this.size != otherdeque.size()) {
            return false;
        }
        for (int i = 0; i < this.size; i++) {
            if (!this.get(i).equals(otherdeque.get(i))) {
                return false;
            }
        }
        return true;
    }

    //iterators
    private class DequeIterator implements Iterator<T> {
        int curpos = 0;

        @Override
        public boolean hasNext() {
            return curpos < size;
        }

        @Override
        public T next() {
            T returnitem = items[(curpos + firstIndex) % capacity];
            curpos += 1;
            return returnitem;
        }
    }
}
