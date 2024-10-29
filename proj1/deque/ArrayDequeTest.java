package deque;

import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayDequeTest {
    @Test
    public void ResizeTest() {
        ArrayDeque<Integer> a = new ArrayDeque<>();
        for (int i = 0; i < 10000; i++) {
            a.addFirst(i);
        }
        for (int i = 9999; i >= 0; i--) {
            assertEquals(i, (int) a.removeFirst());
        }
    }

    @Test
    public void addTest1() {
        ArrayDeque<Integer> a = new ArrayDeque<>();
        a.addFirst(1);
        a.addFirst(2);
        a.addLast(3);
        a.addLast(4);
        a.printDeque();
        assertEquals(4, (int) a.removeLast());
        assertEquals(3, (int) a.removeLast());
        assertEquals(1, (int) a.removeLast());
        assertEquals(2, (int) a.removeLast());
    }

    @Test
    public void addTest2() {
        ArrayDeque<Integer> a = new ArrayDeque<>();
        for (int i = 0; i < 9; i++) {
            a.addFirst(i);
        }

        for (int i = 8; i >= 0; i--) {
            assertEquals(i, (int) a.removeFirst());
        }
    }

    @Test
    public void equalsTest() {
        ArrayDeque<Integer> a = new ArrayDeque<>();
        LinkedListDeque<Integer> b = new LinkedListDeque<>();
        for (int i = 1; i < 10; i++) {
            a.addFirst(i);
            b.addFirst(i);
        }
        assertTrue(a.equals(b));
    }

    @Test
    public void Test1(){
        ArrayDeque<Integer> a = new ArrayDeque<>();
        a.addLast(0);
        int b = a.removeFirst();
        assertEquals(0,b);
    }
}
