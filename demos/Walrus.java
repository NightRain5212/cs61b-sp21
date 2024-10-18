public class Walrus {
    int weight;
    double tusksize;
    public  Walrus(int w , double tusksize){
        this.weight = w;
        this.tusksize = tusksize;
    }

    public static void main(String[] args){
        //a与b指向同一个对象
        // a 实际上储存的是新建对象的地址
        Walrus a = new Walrus(1000,8.3);
        Walrus b;
        //将a储存的地址赋给b，所以a与b指向同一个对象。
        b = a;
        b.weight = 5;
        System.out.println(a.weight);

        //x 与 y 是不同的整数
        //原始类型:int,double,boolean,short,long,byte,char,float，使用时一般为值传递。
        //引用类型：非原始类型即为引用类型，使用时（new）为引用传递。（传递的是对象的地址，而非对象本身）
        //传入参数时的情况也有所不同。
        int x = 50;
        int y;
        y = x;
        y = 3;
        System.out.println(y);
    }
}

