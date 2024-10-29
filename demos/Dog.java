public class Dog {
    public int weightinpounds;
    public static String binomen = "Canis familiaris";

    public Dog(int w){
        this.weightinpounds = w;
    }

    public void makeNoise(){
        if(weightinpounds <10){
            System.out.println("yipyipyip!");
        } else if (weightinpounds < 30){
            System.out.println("bark!");
        }else {
            System.out.println("acrooooo!");
        }
    }
    public static Dog maxDog(Dog d1,Dog d2){
        if(d1.weightinpounds > d2.weightinpounds){
            return d1;
        }else{
            return d2;
        }
    }

    public Dog maxDog(Dog d){
        if(this.weightinpounds < d.weightinpounds){
            return d;
        }
        else{
            return this;
        }
    }

//    public static void main(String[] args){
//        Dog chester = new Dog(17);
//        Dog yusof = new Dog(150);
//        Dog larger = Dog.maxDog(chester,yusof);
//        larger.makeNoise();
//        Dog larger2 = chester.maxDog(yusof);
//        larger2.makeNoise();
//        System.out.println(Dog.binomen);
//        System.out.println(chester.binomen);
//
//    }



}