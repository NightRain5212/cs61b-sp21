package deque;

import java.util.Iterator;

public class LinkedListDeque<Item> implements Iterable<Item>, Deque<Item> {

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
    public void addFirst(Item t) {
//        sentinel.next = new Node(sentinel,t,sentinel.next);
//        size += 1;
//        if(size == 1){
//            sentinel.prev = sentinel.next;
//        }
//        sentinel.next.next.prev = sentinel.next;
        Node newNode = new Node(sentinel, t, sentinel.next);
        sentinel.next.prev = newNode;
        sentinel.next = newNode;
        size += 1;

    }

    // addlast
    @Override
    public void addLast(Item t) {
//        sentinel.prev = new Node(sentinel.prev,t,sentinel);
//        if(size == 1){
//            sentinel.next = sentinel.prev;
//        }
//        sentinel.prev.prev.next = sentinel.prev;
        Node newNode = new Node(sentinel.prev, t, sentinel);
        sentinel.prev.next = newNode;
        sentinel.prev = newNode;
        size += 1;
    }

    //removefirst
    @Override
    public Item removeFirst() {
        if (size == 0) {
            return null;
        }
        Item returnitem = sentinel.next.item;
        this.sentinel.next = sentinel.next.next;
        this.sentinel.next.prev = sentinel;
        size -= 1;
        return returnitem;

    }

    //removelast
    @Override
    public Item removeLast() {
        if (size == 0) {
            return null;
        }
        Item returnitem = sentinel.prev.item;
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

//    // isEmpty
//    @Override
//    public boolean isEmpty(){
//        if(size == 0){
//            return true;
//        }else  {
//            return false;
//        }
//    }

    //get
    @Override
    public Item get(int i) {
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
        return p.item;
    }

    //getRecursive
    public Item getRecursive(int i) {
        if (i == 0) {
            return recursiveget(0, sentinel.next);
        } else {
            return recursiveget(i, sentinel.next);
        }
    }

    private Item recursiveget(int i, Node start) {
        if (i == 0) {
            return start.item;
        } else {
            return recursiveget(i - 1, start.next);
        }
    }

    //printDeque
    @Override
    public void printDeque() {
//        Node p = this.sentinel.next;
//        while(p != this.sentinel){
//            System.out.print(p.item + " ");
//            p = p.next;
//        }
        for (Item i : this) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    //Iterator
    public Iterator<Item> iterator() {
        return new dequeiterator();
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
            if (this.get(i) != otherdeque.get(i)) {
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
        Item item;
        //指向后面一个节点
        Node next;

        //构造函数
        public Node(Node prev, Item item, Node next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }

        public Node() {
            item = null;
            next = this;
            prev = next;
        }
    }

    private class dequeiterator implements Iterator<Item> {
        Node p = sentinel.next;

        @Override
        public boolean hasNext() {
            if (p != sentinel) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public Item next() {
            Item returnitem = p.item;
            p = p.next;
            return returnitem;
        }
    }

}
