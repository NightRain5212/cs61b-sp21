//简单链表的实现
public class Intlist {
    public int first;
    //实际上储存的是引用
    public Intlist rest;

    //构造函数
    public Intlist(int f,Intlist r){
        this.first = f;
        this.rest = r;
    }

    //递归计算数组大小
    public int size()
    {
        if(this.rest == null){
            return 1;
        }
        else {
            return 1+this.rest.size();
        }
    }
    //迭代法求长度
    public int iterativesize(){
        int i = 0;
        // p 是指向自己的指针
        Intlist p = this;
        while (p != null){
            p = p.rest;
            i+=1;
        }
        return i;
    }

    //递归法访问元素
    public int get(int i){
        if(i == 0){
            return this.first;
        }
        else{
            return this.rest.get(i-1);
        }
    }

    public static void main(String[] args){
//        Intlist L = new Intlist();
//        L.first = 3;
//        L.rest = new Intlist();
//        L.rest.first = 4;
//        L.rest.rest = null;
        // [3 , rest] rest -> [4,rest] rest -> null
        Intlist L = new Intlist(10,null);
        //在列表的前面构建列表
        L = new Intlist(20,L);
        L = new Intlist(30,L);
//        System.out.println(L.size());
        System.out.println(L.iterativesize());
        System.out.println(L.get(2));


    }
}
