import java.util.Arrays;

public class Ex2 {
    public static int max(int[] m){
        int i = 1;
        int max = m[0];
        while(i<m.length){
            if(max<m[i]){
                max = m[i];
            }
            i++;
        }
        return max;
    }
    public static void main(String[] args){
        int[] numbers = new int[] {9,2,15,2,22,10,6};
        int max1 = max(numbers);
        System.out.println(max1);

    }
}