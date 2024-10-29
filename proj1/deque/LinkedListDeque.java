package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {

    private Node sentinel;
    //实时跟踪大小
    private int size;

    //建立一个空deque
    public LinkedListDeque() {
        sentinel = new Node();
        size = 0;
    }

    // addfirst
    @Override
    public void addFirst(T t) {
        Node newNode = new Node(sentinel, t, sentinel.next);
        sentinel.next.prev = newNode;
        sentinel.next = newNode;
        size += 1;

    }

    // addlast
    @Override
    public void addLast(T t) {
        Node newNode = new Node(sentinel.prev, t, sentinel);
        sentinel.prev.next = newNode;
        sentinel.prev = newNode;
        size += 1;
    }

    //removefirst
    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T returnitem = sentinel.next.item;
        this.sentinel.next = sentinel.next.next;
        this.sentinel.next.prev = sentinel;
        size -= 1;
        return returnitem;

    }

    //removelast
    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T returnitem = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size -= 1;
        return returnitem;
    }

    // size
    @Override
    public int size() {
        return this.size;
    }

    //get
    @Override
    public T get(int i) {
        if (i < 0 || i > size) {
            return null;
        }
        if (size == 0) {
            return null;
        }
        Node p = sentinel.next;
        while (i > 0) {
            p = p.next;
            i--;
        }
        System.out.println(p.item);
        return p.item;
    }

    //getRecursive
    public T getRecursive(int i) {
        if (i == 0) {
            return recursiveget(0, sentinel.next);
        } else {
            return recursiveget(i, sentinel.next);
        }
    }

    private T recursiveget(int i, Node start) {
        if (i == 0) {
            return start.item;
        } else {
            return recursiveget(i - 1, start.next);
        }
    }

    //printDeque
    @Override
    public void printDeque() {
        for (T i : this) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    //Iterator
    public Iterator<T> iterator() {
        return new Dequeiterator();
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

    //建立链表的节点
    private class Node {
        //指向之前一个节点
        Node prev;
        //包含的元素
        T item;
        //指向后面一个节点
        Node next;

        //构造函数
        Node(Node prev, T item, Node next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }

        Node() {
            item = null;
            next = this;
            prev = next;
        }
    }

    private class Dequeiterator implements Iterator<T> {
        Node p = sentinel.next;

        @Override
        public boolean hasNext() {
            return p != sentinel;
        }

        @Override
        public T next() {
            T returnitem = p.item;
            p = p.next;
            return returnitem;
        }
    }

}
