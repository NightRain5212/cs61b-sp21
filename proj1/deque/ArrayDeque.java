package deque;

import java.util.Iterator;

public class ArrayDeque<Item> implements Iterable<Item>, Deque<Item> {
    //跟踪大小
    private int size;
    //存储数据的数组
    private Item[] items;
    //数组容量
    private int capacity = 8;


    //记录首元素的位置
    private int firstIndex;
    //末端元素位置
    private int lastIndex;
    //构造函数
    public ArrayDeque() {
        size = 0;
        items = (Item[]) new Object[capacity];
        firstIndex = 0;
        lastIndex = capacity - 1;
    }

    //addFirst
    @Override
    public void addFirst(Item t) {
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
    public void addLast(Item t) {
        if(size == capacity) {
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
    public Item removeFirst() {
        if(size == 0){
            return null;
        }
        //记录返回值并清空
        Item returnitem = items[firstIndex];
        items[firstIndex] = null;
        //更新大小
        size -= 1;
        //更新指向
        firstIndex = (firstIndex + 1 + capacity) % capacity;
        //调整数组大小
        if(size < capacity/4) {
            resize(capacity / 4);
        }
        return returnitem;
    }

    //removeLast
    @Override
    public Item removeLast() {
        if(size == 0) {
            return null;
        }
        Item returnitem = items[lastIndex];
        items[lastIndex] = null;
        //调整数组大小

        //更新指向
        lastIndex = (lastIndex - 1 + capacity) % capacity;
        //更新大小
        size -= 1;
        if(size < capacity/4) {
            resize(capacity / 4);
        }
        return returnitem;
    }

    //size
    @Override
    public int size () {
        return size;
    }

    //get
    @Override
    public Item get(int i) {
        if(i < 0 || i >= size){
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
        for(Item i : this) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    //iterators
    private class dequeIterator implements Iterator<Item> {
        int curpos = 0;
        @Override
        public boolean hasNext() {
            return curpos < size;
        }
        @Override
        public Item next() {
            Item returnitem = items[(curpos + firstIndex) % capacity];
            curpos += 1;
            return returnitem;
        }
    }
    @Override
    public Iterator<Item> iterator() {
        return new dequeIterator();
    }


    //resize扩容
    public void resize(int newcapacity) {
        Item[] newitems = (Item[]) new Object[newcapacity];
        //复制队列元素
        int j = (firstIndex + (newcapacity - capacity)) % newcapacity;
        for(int i = firstIndex;i != lastIndex; i=(i + 1) % capacity,j = (j + 1) % newcapacity) {
            newitems[j] = items[i];
        }
        newitems[j] = items[lastIndex];

        //更新指向
        firstIndex = firstIndex + newcapacity - capacity;
        lastIndex = j;

        //更新引用
        items = newitems;
        //更新容量大小
        capacity = newcapacity;
    }


    @Override
    public boolean equals(Object o) {
        if( o == null){
            return false;
        }
        if (this == o){
            return true;
        }
        if(! (o instanceof Deque)){
            return false;
        }
        Deque otherdeque = (Deque) o;
        if(this.size != otherdeque.size()){
            return false;
        }
        for(int i = 0;i<this.size;i++) {
            if(this.get(i) != otherdeque.get(i)){
                return false;
            }
        }
        return true;
    }
}
