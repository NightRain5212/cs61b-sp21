public class Ex1b {
    public static void DrawTriangle(int N){
        for(int i = 0;i < N;i++){
            for(int j = 0;j <= i ;j++){
                System.out.print("*");
            }
            System.out.print("\n");
        }
    }
    public static void main(String[] args) {
        DrawTriangle(10);
    }
}