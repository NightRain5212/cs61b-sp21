public class AList<cheese> {

    //不变量：代码运行时始终为真

    //储存元素
    private cheese[] items;
    //记录大小
    private int size;

    //空列表创建
    public AList(){
        //容器容量上限为100

        //泛化类型不能直接实例化数组列表
//        items = new cheese[100];  --wrong
        items = (cheese[]) new Object[100];
        size = 0;
    }

    //addlast
    public void addlast(cheese x){
        if(size == items.length){
            // 时间花费可能比较大（尤其是数组大小很大的情况）--平方阶复杂度而SLList为线性阶复杂度
            //resize(size+1);

            //改善方法：多用空间消耗以减少时间消耗(当数组大小很大的时候内存浪费多)
            resize(size*2);
        }
        items[size] = x;
        size += 1;
    }

    //getlast
    public cheese getlast(){
        return items[size-1];
    }

    //get
    public cheese get(int i){
        return items[i-1];
    }

    //size
    public int size(){
        return this.size;
    }

    //removelast
    //只需考虑实现，无需关注运行过程，只需保证末端元素无法被访问即可
    //删除引用类型时，仅减少size不会删去对应元素的引用，该对象在内存中仍然存在，需要设置为null。
    public cheese removelast(){
        cheese x = getlast();
        size -= 1;
        return x;
    }

    // 实现数组列表长度的增加--Resizing
    // 创建一个更大的列表，把原列表已有的元素拷贝过来即可
    private void resize(int capacity){
        cheese[] a = (cheese[]) new Object[capacity];
        System.arraycopy(items,0,a,0,size);
        items = a;
    }




}
