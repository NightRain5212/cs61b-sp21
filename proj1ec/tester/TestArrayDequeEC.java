package tester;

import static org.junit.Assert.*;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {

    @Test
    public void myTest() {
        StudentArrayDeque<Integer> a = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> b = new ArrayDequeSolution<>();
        String sequence = "";
        boolean ispassed = true;
        Integer c;
        Integer d;
        for (int i = 0; i < 10000; i++) {
            int t = StdRandom.uniform(0, 4);
            if (t == 0) {
                a.addLast(0);
                b.addLast(0);
                sequence += "addLast(0)\n";
            } else if (t == 1) {
                a.addFirst(2);
                b.addFirst(2);
                sequence += "addFirst(2)\n";
            } else if (t == 2) {
                if(a.isEmpty()){
                    sequence += "isEmpty()\n";
                    continue;
                }
                c = a.removeFirst();
                d = b.removeFirst();
                sequence += "removeFirst()\n";
                assertEquals(sequence, d, c);

            } else if (t == 3) {
                if(a.isEmpty()){
                    sequence += "isEmpty()\n";
                    continue;
                }
                c = a.removeLast();
                d = b.removeLast();
                sequence += "removeLast()\n";
                assertEquals(sequence, d, c);
            }
        }
    }
}
