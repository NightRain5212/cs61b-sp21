package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>,V> implements Map61B<K,V>{

    //节点类
    private class BSTNode {
        K key;
        V value;
        BSTNode leftNode;
        BSTNode rightNode;
        //构造函数
        public BSTNode(K k,V v,BSTNode left,BSTNode right){
            key = k;
            value = v;
            leftNode = left;
            rightNode = right;
        }
    }
    //大小
    private int size;
    //根节点
    private BSTNode rootNode;
    //构造空树
    public BSTMap () {
        size = 0;
        rootNode = null;
    }

    @Override
    public void clear() {
        size = 0;
        rootNode = null;
    }

    @Override
    public boolean containsKey(K key) {
        return containskey(key,rootNode);
    }
    //内置递归实现
    private boolean containskey(K key,BSTNode node){
        if(node == null) {
            return false;
        }
        if(node.key.compareTo(key) == 0) {
            return true;
        } else if (node.key.compareTo(key) < 0) {
            return containskey(key,node.rightNode);
        } else {
            return containskey(key,node.leftNode);
        }
    }

    @Override
    public V get(K key) {
        if(!this.containsKey(key)) {
            return null;
        } else {
            return getvalue(key,rootNode);
        }
    }
    //内置递归实现
    private V getvalue(K key,BSTNode node){
        if (node == null) {
            return null;
        }
        if(node.key.compareTo(key) == 0) {
            return node.value;
        } else if (node.key.compareTo(key) < 0) {
            return getvalue(key,node.rightNode);
        } else {
            return getvalue(key,node.leftNode);
        }
    }
    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key,V value) {
        if(this.containsKey(key)) {
            return;
        }
        putpair(key,value,rootNode);
        size += 1;
    }
    //内置递归实现
    private void putpair(K k,V v,BSTNode node) {
        if(rootNode == null){
            rootNode = new BSTNode(k,v,null,null);
            return;
        }else {
            if (node.key.compareTo(k) < 0){
                if(node.rightNode == null) {
                    node.rightNode = new BSTNode(k,v,null,null);
                } else {
                    putpair(k,v,node.rightNode);
                }
            } else {
                if(node.key.compareTo(k) > 0) {
                    if(node.leftNode == null) {
                        node.leftNode = new BSTNode(k,v,null,null);
                    } else {
                        putpair(k,v,node.leftNode);
                    }
                }
            }
        }
    }

    //printinorder
    public void printInOrder(){
        printOrder(rootNode);
    }
    //内置实现
    private void printOrder(BSTNode node) {
        if(node == null) {
            return;
        } else {
            printOrder(node.leftNode);
            System.out.println(node.key);
            printOrder(node.rightNode);
        }
    }


    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }


    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }
    
}
