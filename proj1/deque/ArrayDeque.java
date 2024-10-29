package deque;

import java.util.Iterator;

public class ArrayDeque<Item> implements Iterable<Item>{
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
    public ArrayDeque(){
        size = 0;
        items = (Item[]) new Object[capacity];
        firstIndex = 0;
        lastIndex = -1;
    }

    //addFirst
    public void addFirst(Item t){
        if(size == capacity){
            resize(capacity * 2);
        }
        //更新指向，防止负数
        firstIndex = (firstIndex - 1 + capacity) % capacity;
        items[firstIndex] = t;
        //更新大小
        size += 1;
    }

    //addLast
    public void addLast(Item t){
        if(size == capacity){
            resize(capacity*2);
        }
        //更新指向
        lastIndex = (lastIndex+1+capacity)%capacity;
        items[lastIndex] = t;
        //更新大小
        size += 1;
    }

    //removeFirst
    public Item removeFirst(){
        if(size == 0){
            return null;
        }
        //记录返回值并清空
        Item returnitem = items[firstIndex];
        items[firstIndex] = null;
        //更新大小
        size -= 1;
        //更新指向
        firstIndex = (firstIndex+1+capacity)%capacity;
        //调整数组大小
        if(size < capacity/4){
            resize(capacity/4);
        }
        return returnitem;
    }

    //removeLast
    public Item removeLast(){
        if(size == 0){
            return null;
        }
        Item returnitem = items[lastIndex];
        items[lastIndex] = null;
        //更新指向
        lastIndex = (lastIndex - 1 + capacity)%capacity;
        //更新大小
        size -= 1;
        //调整数组大小
        if(size < capacity/4){
            resize(capacity/4);
        }
        return returnitem;
    }

    //size
    public int size (){
        return size;
    }

    //get
    public Item get(int i){
        return items[(firstIndex + i)%capacity];
    }

    //isEmpty
    public boolean isEmpty(){
        if(size == 0){
            return true;
        }
        return false;
    }

    //printDeque
    public void printDeque(){
        for(Item i : this){
            System.out.print(i+" ");
        }
        System.out.println();
    }

    //iterators
    private class dequeIterator implements Iterator<Item> {
        int curpos = 0;
        @Override
        public boolean hasNext(){
            return curpos < size;
        }
        @Override
        public Item next(){
            Item returnitem = items[(curpos + firstIndex)%capacity];
            curpos +=1;
            return returnitem;
        }
    }
    @Override
    public Iterator<Item> iterator(){
        return new dequeIterator();
    }

    //equals
    @Override
    public boolean equals(Object o){
        if(o instanceof ArrayDeque otherarraydeque){
            int i = 0;
            int j = 0;
            if(this.size != otherarraydeque.size){
                return false;
            }
            for(;i<size;i++,j++){
                if(this.items[i] != otherarraydeque.items[i]){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    //resize扩容
    public void resize(int newcapacity){
        Item[] newitems = (Item[]) new Object[newcapacity];
        //复制队列元素
        int j = (firstIndex + (newcapacity - capacity))% newcapacity;
        for(int i = firstIndex;i >= lastIndex; i=(i+1)%capacity,j++){
            newitems[j] = items[i];
            if(i == lastIndex){
                newitems[j] = items[i];
                break;
            }
        }

        //更新引用
        items = newitems;
        //更新容量大小
        capacity = newcapacity;
    }

//    public static void main(String[] args){
//        ArrayDeque<Integer> l =new ArrayDeque<>();
//        System.out.println(l.isEmpty());
//        l.addFirst(1);
//        l.addFirst(2);
//        l.addFirst(3);
//        l.addLast(4);
//        l.addLast(5);
//        l.addLast(6);
//        System.out.println(l.size());
//        l.printDeque();
//        System.out.println(l.removeFirst());
//        System.out.println(l.removeLast());
//        System.out.println(l.removeFirst());
//        System.out.println(l.removeLast());
//        System.out.println(l.removeFirst());
//        System.out.println(l.removeFirst());
//        System.out.println(l.size());
//        l.printDeque();
//    }
}
