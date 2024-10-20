//单向链表实现
//数据抽象的体现，只需了解接口，无需理解运作细节
public class SSList {
  // private 防止类外对类内元素的访问，以进行一定程度的保护
  // 嵌套类，链表节点
  // 嵌套静态类型，意味着内部类型的实例变量不能访问外部类的元素
  private class Intnode {
    public int item;
    public Intnode next;

    public Intnode(int i, Intnode n) {
      item = i;
      next = n;
    }
  }

//  private Intnode first;

  // getfirst
  public int getfirst() {
    return sentinel.next.item;
  }

  // addfirst
  public void addfirst(int a) {
    this.sentinel.next = new Intnode(a, sentinel.next);
    sentinel.item += 1;
  }

  // addlast
  public void addlast(int a) {
    Intnode p = this.sentinel.next;
    while (p.next != null) {
      p = p.next;
    }
    p.next = new Intnode(a, null);
    sentinel.item += 1;
  }

  // size(可以分为两部分：外部接口和内部实现)
  // private int size(Intnode i) {
  // if (i.next == null) {
  // return 1;
  // } else {
  // return 1 + size(i.next);
  // }
  // }
  //
  // public int size() {
  // return size(first);
  // }

  // size的改善，引入计数变量
  // private int size = 0;

  public int size() {
    return this.sentinel.item;
  }

  // 创建空列表
  public SSList() {
    sentinel = new Intnode(0,null);
  }

  // 解决引入空列表的方法--引入零节点（哨兵节点）
  //第一个节点位于哨兵结点之后
  //哨兵节点的值存放元素个数
  private Intnode sentinel ;

  // main method test
  public static void main(String[] args) {
    SSList l = new SSList();
    l.addfirst(1);
    l.addlast(2);
    int sizel = l.size();
    System.out.println("size:" + sizel);
    System.out.println("firstitem:" + l.getfirst());
  }
}
