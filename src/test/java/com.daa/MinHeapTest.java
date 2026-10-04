package com.daa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MinHeapTest {

    private MinHeap heap;

    @BeforeEach
    void setUp() {
        heap = new MinHeap();
    }

    @Test
    void emptyHeap() {
        assertTrue(heap.isEmpty());
        assertEquals(0, heap.size());
        assertThrows(IllegalStateException.class, () -> heap.peekMin());
        assertThrows(IllegalStateException.class, () -> heap.extractMin());
    }

    @Test
    void singleElement() {
        heap.insert(42);
        assertEquals(1, heap.size());
        assertEquals(42, heap.peekMin());
        assertEquals(42, heap.extractMin());
        assertTrue(heap.isEmpty());
    }

    @Test
    void insertAndPeek() {
        heap.insert(30);
        heap.insert(10);
        heap.insert(20);
        assertEquals(10, heap.peekMin());
        assertTrue(heap.isHeap());
    }

    @Test
    void extractMinOrder() {
        int[] vals = {5, 3, 8, 1, 9, 2, 7};
        for (int v : vals) {
            heap.insert(v);
            assertTrue(heap.isHeap(), "heap property violated after insert " + v);
        }
        int prev = Integer.MIN_VALUE;
        for (int i = 0; i < vals.length; i++) {
            int v = heap.extractMin();
            assertTrue(v >= prev, "not non-decreasing");
            prev = v;
            assertTrue(heap.isHeap(), "heap property violated after extractMin");
        }
        assertTrue(heap.isEmpty());
    }

    @Test
    void duplicates() {
        heap.insert(5);
        heap.insert(5);
        heap.insert(3);
        heap.insert(5);
        assertEquals(3, heap.extractMin());
        assertEquals(5, heap.extractMin());
        assertEquals(5, heap.extractMin());
        assertEquals(5, heap.extractMin());
    }

    @Test
    void compareWithPriorityQueue() {
        Random rnd = new Random(42);
        PriorityQueue<Integer> ref = new PriorityQueue<>();
        for (int i = 0; i < 500; i++) {
            int v = rnd.nextInt(10000);
            heap.insert(v);
            ref.offer(v);
            assertTrue(heap.isHeap());
        }
        while (!ref.isEmpty()) {
            assertEquals(ref.poll().intValue(), heap.extractMin());
            assertTrue(heap.isHeap());
        }
        assertTrue(heap.isEmpty());
    }

    @Test
    void buildHeap() {
        Random rnd = new Random(123);
        int n = 1000;
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = rnd.nextInt();

        MinHeap h = new MinHeap();
        h.buildHeap(arr);
        assertEquals(n, h.size());
        assertTrue(h.isHeap());

        List<Integer> sorted = new ArrayList<>();
        for (int v : arr) sorted.add(v);
        Collections.sort(sorted);

        for (Integer expected : sorted) {
            assertEquals(expected.intValue(), h.extractMin());
        }
    }

    @Test
    void buildHeapVsInserts() {
        Random rnd = new Random(99);
        int n = 2000;
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = rnd.nextInt();

        MinHeap h1 = new MinHeap();
        h1.resetMetrics();
        for (int v : arr) h1.insert(v);
        long compsInsert = h1.getMetrics().getComparisons();

        MinHeap h2 = new MinHeap();
        h2.resetMetrics();
        h2.buildHeap(arr);
        long compsBuild = h2.getMetrics().getComparisons();

        assertTrue(compsBuild < compsInsert,
                "buildHeap comparisons (" + compsBuild + ") should be < n inserts (" + compsInsert + ")");
        assertTrue(h2.isHeap());
    }

    @Test
    void metricsCounted() {
        heap.resetMetrics();
        heap.insert(10);
        heap.insert(5);
        heap.extractMin();
        Metrics m = heap.getMetrics();
        assertTrue(m.getSteps() > 0 || m.getMoves() > 0 || m.getComparisons() > 0);
    }
}