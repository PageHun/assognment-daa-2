import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MyLinkedListTest {
    private MyLinkedList list;

    @BeforeEach
    void setUp() {
        list = new MyLinkedList();
    }

    @Test
    void emptyStructure() {
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
        assertFalse(list.contains(42));
    }

    @Test
    void singleElement() {
        list.add(7);
        assertEquals(1, list.size());
        assertEquals(7, list.get(0));
        assertTrue(list.contains(7));
        assertFalse(list.contains(8));
        assertEquals(7, list.remove(0));
        assertTrue(list.isEmpty());
    }

    @Test
    void addGetContains() {
        list.add(10);
        list.add(20);
        list.add(30);
        assertEquals(3, list.size());
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
        assertEquals(30, list.get(2));
        assertTrue(list.contains(20));
        assertFalse(list.contains(99));
    }

    @Test
    void addAtIndex() {
        list.add(1);
        list.add(3);
        list.add(1, 2);
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
        list.add(0, 0);
        assertEquals(0, list.get(0));
        list.add(4, 4);
        assertEquals(4, list.get(4));
    }

    @Test
    void removeAtIndex() {
        list.add(10);
        list.add(20);
        list.add(30);
        list.add(40);
        assertEquals(20, list.remove(1));
        assertEquals(3, list.size());
        assertEquals(10, list.get(0));
        assertEquals(30, list.get(1));
        assertEquals(40, list.get(2));
        assertEquals(10, list.remove(0));
        assertEquals(40, list.remove(1));
        assertEquals(30, list.remove(0));
        assertTrue(list.isEmpty());
    }

    @Test
    void invalidIndex() {
        list.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(2, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
    }

    @Test
    void duplicates() {
        list.add(5);
        list.add(5);
        list.add(5);
        assertEquals(3, list.size());
        assertTrue(list.contains(5));
        assertEquals(5, list.remove(1));
        assertEquals(2, list.size());
    }

    @Test
    void compareWithLinkedList() {
        Random rnd = new Random(42);
        List<Integer> ref = new LinkedList<>();
        for (int i = 0; i < 300; i++) {
            int v = rnd.nextInt(10000);
            list.add(v);
            ref.add(v);
        }
        for (int i = 0; i < 50; i++) {
            int idx = rnd.nextInt(list.size() + 1);
            int v = rnd.nextInt();
            list.add(idx, v);
            ref.add(idx, v);
        }
        for (int i = 0; i < 50; i++) {
            int idx = rnd.nextInt(list.size());
            assertEquals(ref.remove(idx).intValue(), list.remove(idx));
        }
        assertEquals(ref.size(), list.size());
        for (int i = 0; i < ref.size(); i++) {
            assertEquals(ref.get(i).intValue(), list.get(i));
        }
    }

    @Test
    void metricsCounted() {
        list.resetMetrics();
        list.add(1);
        list.add(2);
        list.get(0);
        list.contains(2);
        Metrics m = list.getMetrics();
        assertTrue(m.getSteps() > 0 || m.getMoves() > 0);
    }
}