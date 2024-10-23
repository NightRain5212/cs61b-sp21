import java.util.stream.StreamSupport;

public class GenericList<pineapple> {

    private class Intnode {
        public pineapple item;
        public Intnode next;

        public Intnode(pineapple i, Intnode n) {
            item = i;
            next = n;
        }
    }

    private Intnode sentinel;
    private int size = 0;
//  private Intnode first;

    // getfirst
    public pineapple getfirst() {
        return sentinel.next.item;
    }

    // addfirst
    public void addfirst(pineapple a) {
        this.sentinel.next = new Intnode(a, sentinel.next);
        size += 1;
    }

    // addlast
    public void addlast(pineapple a) {
        Intnode p = this.sentinel.next;
        while (p.next != null) {
            p = p.next;
        }
        p.next = new Intnode(a, null);
        size += 1;
    }


    public int size() {
        return this.size;
    }

    // 创建空列表
    public GenericList() {
        sentinel = new Intnode(null,null);
    }

    // 解决引入空列表的方法--引入零节点（哨兵节点）
    //第一个节点位于哨兵结点之后
    //哨兵节点的值存放元素个数
    public static void main(String[] args){
        GenericList<String> l = new GenericList<String>();
        l.addfirst("what");
        l.addlast("the");
        l.addlast("dog");
        l.addlast("doin");
        System.out.println(l.getfirst());
        System.out.println(l.size());

    }
}
