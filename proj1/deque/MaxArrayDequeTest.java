package deque;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.assertEquals;

public class MaxArrayDequeTest {

    @Test
    public void Test1() {
        Comparator<Integer> c = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        };
        MaxArrayDeque<Integer> a = new MaxArrayDeque<>(c);
        for (int i = 0; i < 5; i++) {
            a.addFirst(i);
        }
        assertEquals(4, (int) a.max());
    }

    @Test
    public void Test2() {
        Comparator<String> c = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        };
        MaxArrayDeque<String> a = new MaxArrayDeque<>(c);
        for (int i = 0; i < 5; i++) {
            a.addFirst("a");
            a.addLast("b");
        }
        assertEquals("b", (String) a.max());
    }
}
