package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
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
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        AList<Integer> Ns = new AList<>();
        AList<Double> Times = new AList<>();
        AList<Integer> Opcounts = new AList<>();
        for(int a = 1000;a<=128000;a*=2){
            Ns.addLast(a);
        }
        for (int i = 0;i<Ns.size();i++){
            mytest1(Ns,i,Times,Opcounts);
        }
        printTimingTable(Ns,Times,Opcounts);
    }

    public static void mytest1(AList<Integer> Ns , int k ,AList<Double>Times,AList<Integer> Opcounts){
        SLList<Integer> L = new SLList<>();
        for(int i = 0 ;i<Ns.get(k);i++){
            L.addLast(1);
        }
        Stopwatch sw = new Stopwatch();
        double t1,t2;
        int M = 10000;
        t1 = sw.elapsedTime();
        for(int i = 0;i<M;i++){
            L.getLast();
        }
        t2 =sw.elapsedTime();
        Times.addLast(t2-t1);
        Opcounts.addLast(M);

    }

}
