package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove(){
        AListNoResizing<Integer> L1 = new AListNoResizing<>();
        BuggyAList<Integer> L2 = new BuggyAList<>();
        for (int i = 4;i<=6;i++){
            L1.addLast(i);
            L2.addLast(i);
        }
        boolean ispassed = false;
        for(int i = 0;i<3;i++){
            if(L1.removeLast() == L2.removeLast()){
                ispassed = true;
            } else{
                break;
            }
        }
        assertTrue(ispassed);
    }

    @Test
    public void randomizedTest(){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> L1 = new BuggyAList<>();
        int N = 50000;
        boolean ispassed = false;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                L1.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                int size1 = L1.size();
                System.out.println("size: " + size +" "+ size1);
            }else if (operationNumber == 2){
                //getLast
                if(L.size() == 0 || L1.size() == 0){
                    continue;
                }
                int last = L.getLast();
                int last1 = L1.getLast();
                System.out.println("getlast: " + last +" "+ last1);
                if(last1 == last){
                    ispassed = true;
                }else{
                    break;
                }
            }else if (operationNumber == 3){
                //removelast
                if(L.size() == 0 || L1.size() == 0){
                    continue;
                }
                int last = L.removeLast();
                int last1 = L1.removeLast();
                System.out.println("removelast: " + last +" "+ last1);
                if(last == last1){
                    ispassed = true;
                } else {
                    break;
                }
            }
        }
        assertTrue(ispassed);
    }
}
