package tester;

import static org.junit.Assert.*;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {

    @Test
    public void myTest1(){
        StudentArrayDeque<Integer> a = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> b = new ArrayDequeSolution<>();
        boolean ispassed = true;
        for(int i = 0;i<1000;i++){
            double zeroorone = StdRandom.uniform();
            if(zeroorone < 0.5){
                a.addFirst(i);
                b.addFirst(i);
            } else {
                a.addLast(i);
                b.addLast(i);
            }
        }
        for (int i = 0 ; i<a.size();i++){
            if(!a.get(i).equals(b.get(i))) {
                ispassed = false;
            }
        }

        assertTrue(ispassed);
    }

    @Test
    public void myTest2(){
        StudentArrayDeque<Integer> a = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> b = new ArrayDequeSolution<>();
        boolean ispassed = true;
        int c;
        int d;
        for(int i = 0;i<10000;i++){
            int t = StdRandom.uniform(0,4);
            if( t == 0) {
                a.addLast(0);
                b.addLast(0);
                System.out.println("a.addLast(0)");
            } else if ( t == 1 ) {
                a.addFirst(2);
                b.addFirst(2);
                System.out.println("a.addFirst(2)");
            } else if (t == 2) {
                 c = a.removeFirst();
                 d = b.removeFirst();
                if ( c != d) {
                    ispassed = false;
                }
                System.out.println("a.removeFirst()");
            } else if (t == 3){
                 c = a.removeLast();
                 d = b.removeLast();
                if (c != d) {
                    ispassed = false;
                }
                System.out.println("a.removeLast()");
            }
        }

        for( int i = 0;i < a.size() ; i++){
            if ( !a.get(i).equals(b.get(i))) {
                ispassed = false;
            }
        }
        assertTrue(ispassed);

    }
}
