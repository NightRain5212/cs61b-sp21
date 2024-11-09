package hashmap;

import java.security.Key;
import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private int size;

    private double loadFactor;
    private int bucketsSize;

    private HashSet<K> keySet = new HashSet<>();

    /** Constructors */
    public MyHashMap() {
        size = 0;
        buckets = createTable(16);
        loadFactor = 0.75;
    }

    public MyHashMap(int initialSize) {
        size = 0;
        buckets = createTable(initialSize);
        loadFactor = 0.75;
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        size = 0;
        buckets = createTable(initialSize);
        loadFactor = maxLoad;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key,value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {

        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] table = new Collection[tableSize];
        for(int i = 0;i<tableSize;i++) {
            table[i] = createBucket();
        }
        bucketsSize = tableSize;
        return table;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!
    @Override
    public void clear() {
        size = 0;
        buckets = createTable(16);
    }

    @Override
    public boolean containsKey(K key) {
        int hashcode = Math.abs(key.hashCode());
        int index = hashcode % bucketsSize;

        for(Node i : buckets[index]) {
            if(i.key.equals(key)) {
                return true;
            }
        }
        return false;
    }
    //获取键对应的值
    @Override
    public V get(K key) {
        //检查是否存在
        if(!containsKey(key)) {
            return null;
        }
        int hashcode = Math.abs(key.hashCode());
        int index = hashcode%bucketsSize;
        for(Node i : buckets[index]) {
            if(i.key.equals(key)) {
                return i.value;
            }
        }
        return null;
    }
    //返回元素个数
    @Override
    public int size() {
        return size;
    }
    //put
    @Override
    public void put(K key,V value) {
        //获取哈希计算桶索引
        int hashcode = Math.abs(key.hashCode());
        int index = hashcode % bucketsSize;
        //查重更新值
        if(containsKey(key)) {
            for(Node i : buckets[index]) {
                if(i.key.equals(key)) {
                    i.value = value;
                }
            }
            size -=1;
        }
        //添加新节点
        Node newNode = new Node(key,value);
        buckets[index].add(newNode);
        //更新
        size+=1;
        keySet.add(key);
        //哈希表的扩大
        if(size > bucketsSize*loadFactor) {
            int newbucketSize = bucketsSize*2;
            Collection<Node>[] newbuckets = createTable(newbucketSize);
            for(Collection<Node> i : buckets) {
                for(Node j : i) {
                    int hc = Math.abs(j.key.hashCode());
                    int index2 = hc % (newbucketSize);
                    newbuckets[index2].add(j);
                }
            }
            buckets = newbuckets;
        }
    }

    //迭代器迭代键
    private class myIterator implements Iterator<K> {
        private Iterator<K> KeyIterator = keySet.iterator();

        @Override
        public boolean hasNext(){
            return KeyIterator.hasNext();
        }

        @Override
        public K next() {
            return (K)KeyIterator.next();
        }

    }

    //创建迭代器
    public Iterator<K> iterator() {
        return new myIterator();
    }


    public V remove(K key) {
        if(!this.containsKey(key)) {
            return null;
        }
        int hc = Math.abs(key.hashCode());
        int index = hc % bucketsSize;
        for(Node i : buckets[index]) {
            if(i.key.equals(key)) {
                V returnvalue = i.value;
                buckets[index].remove(i);
                return returnvalue;
            }
        }
        return null;
    }

    public V remove(K key, V value) {
        if(!this.containsKey(key)) {
            return null;
        }
        int hc = Math.abs(key.hashCode());
        int index = hc % bucketsSize;
        for(Node i : buckets[index]) {
            if(i.key.equals(key)&&i.value.equals(value)) {
                V returnvalue = i.value;
                buckets[index].remove(i);
                return returnvalue;
            }
        }
        return null;
    }

    public Set<K> keySet() {
        return keySet;
    }
}
