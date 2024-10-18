//单向链表实现
//数据抽象的体现，只需了解接口，无需理解运作细节
public class SSList {
    //private 防止类外对类内元素的访问，以进行一定程度的保护
    //嵌套类，链表节点
    //嵌套静态类型，意味着内部类型的实例变量不能访问外部类的元素
    private class Intnode{
        public int item;
        public Intnode next;
        public Intnode(int i,Intnode n){
            item = i;
            next = n;
        }
    }
    private Intnode first;

    //getfirst

    //addfirst

    //addlast

    //size(可以分为两部分：外部接口和内部实现)

    //size的改善，引入计数变量

    //空列表的实现

    //解决引入空列表的方法--引入零节点（哨兵节点）
}
