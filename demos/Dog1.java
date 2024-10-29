import java.util.Comparator;

public class Dog1 implements Comparable<Dog1> {
    public String name;
    private int size;
    public Dog1(String n ,int s){
        name = n;
        size = s;
    }
    public void bark(){
        System.out.println(name + "bark");
    }

    public int size(){
        return this.size;
    }
    //重写比较方法
    @Override
    public int compareTo(Dog1 o){
        return this.size()-o.size();
    }
    //嵌套比较器类型
    private static class Namecomparator implements Comparator<Dog1>{
        @Override
        public int compare(Dog1 d1,Dog1 d2){
            return d1.name.compareTo(d2.name);
        }
    }
    //获得比较器的接口函数
    public static Comparator<Dog1> getNamecomparator(){
        return new Namecomparator();
    }

    public static void main(String[] args){
        Dog1 d1 = new Dog1("puppy",100);
        Dog1 d2 = new Dog1("hashiqi",200);
        if(d1.compareTo(d2)>0){
            d1.bark();
        } else {
            d2.bark();
        }
        Comparator<Dog1> nc = Dog1.getNamecomparator();
        Dog1 d3 = new Dog1("Oski",200);
        Dog1 d4 = new Dog1("Cerebus",999999);
        int cmp = nc.compare(d3,d4);
        if(cmp > 0){
            d3.bark();
        } else {
            d4.bark();
        }
    }
}
