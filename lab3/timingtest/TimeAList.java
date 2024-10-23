package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }


    public static void timeAListConstruction() {
        // TODO: YOUR CODE HERE
        AList<Integer> Ns = new AList<>();
        AList<Double> Times = new AList<>();
        for(int a = 1000;a<=128000;a*=2){
            Ns.addLast(a);
        }
        for(int j = 0;j<Ns.size();j++){
            mytest(Ns,j,Times);
        }
        printTimingTable(Ns,Times,Ns);

   }

   public static void mytest(AList<Integer> Ns , int k , AList<Double> Times){
        AList<Integer> L = new AList<Integer>();
        Stopwatch sw = new Stopwatch();
        double t1,t2;
        t1 = sw.elapsedTime();
        for(int i = 0;i<Ns.get(k);i++){
            L.addLast(1);
        }
        t2 = sw.elapsedTime();
        Times.addLast(t2-t1);
   }
}











