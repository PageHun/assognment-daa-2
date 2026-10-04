import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DynamicArrayTest {

    private DynamicArray da;

    @BeforeEach
    void setUp() {
        da = new DynamicArray();
    }

    @Test
    void emptyStructure() {
        assertTrue(da.isEmpty());
        assertEquals(0, da.size());
        assertThrows(IndexOutOfBoundsException.class, () -> da.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> da.remove(0));
        assertFalse(da.contains(42));
    }

    @Test
    void singleElement() {
        da.add(7);
        assertEquals(1, da.size());
        assertEquals(7, da.get(0));
        assertTrue(da.contains(7));
        assertFalse(da.contains(8));
        assertEquals(7, da.remove(0));
        assertTrue(da.isEmpty());
    }

    @Test
    void addGetContains() {
        da.add(10);
        da.add(20);
        da.add(30);
        assertEquals(3, da.size());
        assertEquals(10, da.get(0));
        assertEquals(20, da.get(1));
        assertEquals(30, da.get(2));
        assertTrue(da.contains(20));
        assertFalse(da.contains(99));
    }

    @Test
    void addAtIndex() {
        da.add(1);
        da.add(3);
        da.add(1, 2); // insert in middle
        assertEquals(1, da.get(0));
        assertEquals(2, da.get(1));
        assertEquals(3, da.get(2));
        da.add(0, 0); // head
        assertEquals(0, da.get(0));
        da.add(4, 4); // tail
        assertEquals(4, da.get(4));
    }

    @Test
    void removeAtIndex() {
        da.add(10);
        da.add(20);
        da.add(30);
        da.add(40);
        assertEquals(20, da.remove(1));
        assertEquals(3, da.size());
        assertEquals(10, da.get(0));
        assertEquals(30, da.get(1));
        assertEquals(40, da.get(2));
        assertEquals(10, da.remove(0));
        assertEquals(40, da.remove(1));
        assertEquals(30, da.remove(0));
        assertTrue(da.isEmpty());
    }

    @Test
    void invalidIndex() {
        da.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> da.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> da.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> da.add(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> da.add(2, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> da.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> da.remove(1));
    }

    @Test
    void duplicates() {
        da.add(5);
        da.add(5);
        da.add(5);
        assertEquals(3, da.size());
        assertTrue(da.contains(5));
        assertEquals(5, da.remove(1));
        assertEquals(2, da.size());
    }

    @Test
    void growth() {
        for (int i = 0; i < 1000; i++) {
            da.add(i);
        }
        assertEquals(1000, da.size());
        for (int i = 0; i < 1000; i++) {
            assertEquals(i, da.get(i));
        }
    }

    @Test
    void compareWithArrayList() {
        Random rnd = new Random(42);
        List<Integer> ref = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            int v = rnd.nextInt(10000);
            da.add(v);
            ref.add(v);
        }
        for (int i = 0; i < 100; i++) {
            int idx = rnd.nextInt(da.size() + 1);
            int v = rnd.nextInt();
            da.add(idx, v);
            ref.add(idx, v);
        }
        for (int i = 0; i < 100; i++) {
            int idx = rnd.nextInt(da.size());
            assertEquals(ref.remove(idx).intValue(), da.remove(idx));
        }
        assertEquals(ref.size(), da.size());
        for (int i = 0; i < ref.size(); i++) {
            assertEquals(ref.get(i).intValue(), da.get(i));
        }
    }

    @Test
    void metricsCounted() {
        da.resetMetrics();
        da.add(1);
        da.add(2);
        da.get(0);
        da.contains(2);
        Metrics m = da.getMetrics();
        assertTrue(m.getSteps() > 0 || m.getMoves() > 0);
    }
}

