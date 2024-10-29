package deque;

import java.util.Iterator;

public class LinkedListDeque<Item> implements Iterable<Item>{

    //建立链表的节点
     private class Node{
        //指向之前一个节点
        Node prev;
        //包含的元素
        Item item;
        //指向后面一个节点
        Node next;

        //构造函数
        public Node(Node prev,Item item,Node next){
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
        public Node(){
            item = null;
            next = this;
            prev = next;
        }
    }
    private Node sentinel;
    //实时跟踪大小
    private int size;

    //建立一个空deque
    public LinkedListDeque(){
        sentinel = new Node();
        size = 0;
    }

    // addfirst
    public void addFirst(Item t){
//        sentinel.next = new Node(sentinel,t,sentinel.next);
//        size += 1;
//        if(size == 1){
//            sentinel.prev = sentinel.next;
//        }
//        sentinel.next.next.prev = sentinel.next;
        Node newNode = new Node( sentinel,t,sentinel.next);
        sentinel.next.prev = newNode;
        sentinel.next = newNode;
        size += 1;

    }

    // addlast
    public void addLast(Item t){
//        sentinel.prev = new Node(sentinel.prev,t,sentinel);
//        if(size == 1){
//            sentinel.next = sentinel.prev;
//        }
//        sentinel.prev.prev.next = sentinel.prev;
        Node newNode = new Node(sentinel.prev,t,sentinel);
        sentinel.prev.next = newNode;
        sentinel.prev = newNode;
        size += 1;
    }

    //removefirst
    public Item removeFirst(){
        if(size == 0){
            return null;
        }
        Item returnitem = sentinel.next.item;
        this.sentinel.next = sentinel.next.next;
        this.sentinel.next.prev = sentinel;
        size -= 1;
        return returnitem;

    }

    //removelast
    public Item removeLast(){
        if(size == 0){
            return null;
        }
        Item returnitem = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size -= 1;
        return returnitem;
    }

    // isEmpty
    public boolean isEmpty(){
        if(size == 0){
            return true;
        }else  {
            return false;
        }
    }

    // size
    public int size(){
        return this.size;
    }

    //get
    public Item get(int i){
        if(size == 0){
            return null;
        }
        Node p = sentinel.next;
        while (i>0){
            p = p.next;
            i--;
        }
        return p.item;
    }

    //getRecursive
    public Item getRecursive (int i){
        if( i == 0){
            return recursiveget(0,sentinel.next);
        }else{
            return recursiveget(i,sentinel.next);
        }
    }

    private Item recursiveget(int i,Node start){
        if( i == 0){
            return start.item;
        }else{
            return recursiveget(i-1,start.next);
        }
    }

    //printDeque
    public void printDeque(){
//        Node p = this.sentinel.next;
//        while(p != this.sentinel){
//            System.out.print(p.item + " ");
//            p = p.next;
//        }
        for(Item i : this){
            System.out.print(i+" ");
        }
        System.out.println();
    }

    //Iterator
    public Iterator<Item> iterator(){
        return new dequeiterator();
    }

    private class dequeiterator implements Iterator<Item>{
        Node p = sentinel.next;
        @Override
        public boolean hasNext(){
            if(p != sentinel){
                return true;
            }else{
                return false;
            }
        }
        @Override
        public Item next(){
            Item returnitem = p.item;
            p = p.next;
            return returnitem;
        }
    }

    //equals
    @Override
    public boolean equals(Object o){
        if(o instanceof LinkedListDeque ){
            LinkedListDeque otherdeque = (LinkedListDeque)  o;
            Node p1 = this.sentinel.next;
            Node p2 = otherdeque.sentinel.next;
            if(this.size != otherdeque.size){
                return false;
            }
            while (p1!=sentinel){
                if(p1.item != p2.item){
                    return false;
                }else{
                    p1 = p1.next;
                    p2 = p2.next;
                }
            }
            return true;
        }
        return false;
    }




}
