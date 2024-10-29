import java.util.Iterator;

public class Arrayset<T> implements Iterable<T> {
    private T[] items;
    private int size = 0;

    public Arrayset(){
        items = (T[]) new Object[100];
        size = 0;
    }

    public int size(){
        return this.size;
    }

    public boolean contain(T x){
        for(int i = 0;i<size;i++){
            if(items[i] == x){
                return true;
            }
        }
        return false;
    }

    public void add(T x){
        if(!contain(x)){
            items[size] = x;
            size+=1;
        }
    }

    //嵌套迭代器类
    private class ArraysetIterator implements Iterator<T>{
        //实现接口函数重写
        private int curpos = 0;
        @Override
        public boolean hasNext(){
            return curpos<size;
        }
        @Override
        public T next(){
            T returnitem = items[curpos];
            curpos+=1;
            return returnitem;
        }
    }

    //返回迭代器的接口
    public Iterator<T> iterator(){
        return new ArraysetIterator();
    }

    //重写toString
    @Override
    public String toString(){
//        String output = "[";
//        //注意这里使用this的增强for循环，而不是items的
//        for(T i :this){
//            output += i.toString() + " ";
//        }
//        output += "]";
//        return output;

        //使用内置的字符串生成类来加快运行速度
        StringBuilder op = new StringBuilder();
        op.append("[ ");
        for (T i : this){
            op.append(i+" ");
        }
        op.append("]");
        return op.toString();

    }
    //重写equals
    @Override
    public boolean equals(Object o){
        //判断对象的类型，如果是的话则会创建一个对应类型的变量otherarrayset
        if(o instanceof Arrayset otherarrayset){
            if(this.size != otherarrayset.size){
                return false;
            }
            for (T i : this){
                if(!otherarrayset.contain(i)){
                    return false;
                }
            }
        }
        return true;
    }


    public static void main(String[] args){
        Arrayset<Integer> set1 = new Arrayset<>();
        set1.add(5);
        set1.add(13);
        set1.add(23);
//        for (int i : set){
//            System.out.println(i);
//        }
        System.out.println(set1.toString());
        Arrayset<Integer> set2 = new Arrayset<>();
        set2.add(5);
        set2.add(13);
        set2.add(23);
        System.out.println(set1.equals(set2));
    }
}