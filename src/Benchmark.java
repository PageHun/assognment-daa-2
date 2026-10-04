import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Benchmark {

    private static final int[] SIZES = {100, 1_000, 10_000, 100_000};
    private static final int WARMUP_RUNS = 2;
    private static final int MEASURE_RUNS = 5;
    private static final long SEED = 42L;

    public static void main(String[] args) throws Exception {
        List<String> rows = new ArrayList<>();
        rows.add("workload,variant,structure,n,time_ms,steps,moves,comparisons");

        System.out.println("=== DAA Assignment 2 Benchmark ===");
        System.out.println("Seed = " + SEED);
        System.out.println();

        System.out.println("W1 - Random Access");
        for (int n : SIZES) {
            runW1(n, rows);
        }

        System.out.println("W2 - Search");
        for (int n : SIZES) {
            runW2(n, rows);
        }

        System.out.println("W3 - Insert & Remove");
        for (int n : SIZES) {
            runW3(n, "head", rows);
            runW3(n, "middle", rows);
        }

        System.out.println("W4 - Priority Processing");
        for (int n : SIZES) {
            runW4(n, rows);
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter("results.csv"))) {
            for (String r : rows) {
                pw.println(r);
            }
        }
        System.out.println();
        System.out.println("Results written to results.csv");
        System.out.println("Done.");
    }

    private static void runW1(int n, List<String> rows) {
        Random rnd = new Random(SEED);
        int[] data = new int[n];
        for (int i = 0; i < n; i++) data[i] = rnd.nextInt();

        {
            long[] times = new long[MEASURE_RUNS];
            Metrics last = null;
            for (int run = 0; run < WARMUP_RUNS + MEASURE_RUNS; run++) {
                DynamicArray da = new DynamicArray(n);
                for (int v : data) da.add(v);
                da.resetMetrics();
                Random r = new Random(SEED + 1);
                long t0 = System.nanoTime();
                for (int i = 0; i < 10_000; i++) {
                    int idx = r.nextInt(n);
                    da.get(idx);
                }
                long t1 = System.nanoTime();
                if (run >= WARMUP_RUNS) {
                    times[run - WARMUP_RUNS] = t1 - t0;
                    last = da.getMetrics().copy();
                }
            }
            long medianNs = median(times);
            rows.add(String.format("W1,-,DynamicArray,%d,%.3f,%d,%d,%d",
                    n, medianNs / 1_000_000.0, last.getSteps(), last.getMoves(), last.getComparisons()));
            System.out.printf("  n=%d DynamicArray  median=%.3f ms  %s%n", n, medianNs / 1e6, last);
        }

        {
            long[] times = new long[MEASURE_RUNS];
            Metrics last = null;
            for (int run = 0; run < WARMUP_RUNS + MEASURE_RUNS; run++) {
                MyLinkedList ll = new MyLinkedList();
                for (int v : data) ll.add(v);
                ll.resetMetrics();
                Random r = new Random(SEED + 1);
                long t0 = System.nanoTime();
                for (int i = 0; i < 10_000; i++) {
                    int idx = r.nextInt(n);
                    ll.get(idx);
                }
                long t1 = System.nanoTime();
                if (run >= WARMUP_RUNS) {
                    times[run - WARMUP_RUNS] = t1 - t0;
                    last = ll.getMetrics().copy();
                }
            }
            long medianNs = median(times);
            rows.add(String.format("W1,-,MyLinkedList,%d,%.3f,%d,%d,%d",
                    n, medianNs / 1_000_000.0, last.getSteps(), last.getMoves(), last.getComparisons()));
            System.out.printf("  n=%d MyLinkedList  median=%.3f ms  %s%n", n, medianNs / 1e6, last);
        }
    }

    private static void runW2(int n, List<String> rows) {
        Random rnd = new Random(SEED);
        int[] data = new int[n];
        for (int i = 0; i < n; i++) data[i] = rnd.nextInt(1_000_000);

        int[] queries = new int[1000];
        for (int i = 0; i < 500; i++) {
            queries[i] = data[rnd.nextInt(n)];
        }
        for (int i = 500; i < 1000; i++) {
            queries[i] = 2_000_000 + rnd.nextInt(1_000_000); // absent
        }

        {
            long[] times = new long[MEASURE_RUNS];
            Metrics last = null;
            for (int run = 0; run < WARMUP_RUNS + MEASURE_RUNS; run++) {
                DynamicArray da = new DynamicArray(n);
                for (int v : data) da.add(v);
                da.resetMetrics();
                long t0 = System.nanoTime();
                for (int q : queries) da.contains(q);
                long t1 = System.nanoTime();
                if (run >= WARMUP_RUNS) {
                    times[run - WARMUP_RUNS] = t1 - t0;
                    last = da.getMetrics().copy();
                }
            }
            long medianNs = median(times);
            rows.add(String.format("W2,-,DynamicArray,%d,%.3f,%d,%d,%d",
                    n, medianNs / 1_000_000.0, last.getSteps(), last.getMoves(), last.getComparisons()));
            System.out.printf("  n=%d DynamicArray  median=%.3f ms  %s%n", n, medianNs / 1e6, last);
        }

        {
            long[] times = new long[MEASURE_RUNS];
            Metrics last = null;
            for (int run = 0; run < WARMUP_RUNS + MEASURE_RUNS; run++) {
                MyLinkedList ll = new MyLinkedList();
                for (int v : data) ll.add(v);
                ll.resetMetrics();
                long t0 = System.nanoTime();
                for (int q : queries) ll.contains(q);
                long t1 = System.nanoTime();
                if (run >= WARMUP_RUNS) {
                    times[run - WARMUP_RUNS] = t1 - t0;
                    last = ll.getMetrics().copy();
                }
            }
            long medianNs = median(times);
            rows.add(String.format("W2,-,MyLinkedList,%d,%.3f,%d,%d,%d",
                    n, medianNs / 1_000_000.0, last.getSteps(), last.getMoves(), last.getComparisons()));
            System.out.printf("  n=%d MyLinkedList  median=%.3f ms  %s%n", n, medianNs / 1e6, last);
        }
    }

    private static void runW3(int n, String variant, List<String> rows) {
        Random rnd = new Random(SEED);
        int[] data = new int[n];
        for (int i = 0; i < n; i++) data[i] = rnd.nextInt();

        {
            long[] times = new long[MEASURE_RUNS];
            Metrics last = null;
            for (int run = 0; run < WARMUP_RUNS + MEASURE_RUNS; run++) {
                DynamicArray da = new DynamicArray(n + 2000);
                for (int v : data) da.add(v);
                da.resetMetrics();
                long t0 = System.nanoTime();
                if ("head".equals(variant)) {
                    for (int i = 0; i < 1000; i++) {
                        da.add(0, rnd.nextInt());
                        da.remove(0);
                    }
                } else {
                    for (int i = 0; i < 1000; i++) {
                        int mid = da.size() / 2;
                        da.add(mid, rnd.nextInt());
                        da.remove(mid);
                    }
                }
                long t1 = System.nanoTime();
                if (run >= WARMUP_RUNS) {
                    times[run - WARMUP_RUNS] = t1 - t0;
                    last = da.getMetrics().copy();
                }
            }
            long medianNs = median(times);
            rows.add(String.format("W3,%s,DynamicArray,%d,%.3f,%d,%d,%d",
                    variant, n, medianNs / 1_000_000.0, last.getSteps(), last.getMoves(), last.getComparisons()));
            System.out.printf("  n=%d %s DynamicArray  median=%.3f ms  %s%n", n, variant, medianNs / 1e6, last);
        }

        {
            long[] times = new long[MEASURE_RUNS];
            Metrics last = null;
            for (int run = 0; run < WARMUP_RUNS + MEASURE_RUNS; run++) {
                MyLinkedList ll = new MyLinkedList();
                for (int v : data) ll.add(v);
                ll.resetMetrics();
                long t0 = System.nanoTime();
                if ("head".equals(variant)) {
                    for (int i = 0; i < 1000; i++) {
                        ll.add(0, rnd.nextInt());
                        ll.remove(0);
                    }
                } else {
                    for (int i = 0; i < 1000; i++) {
                        int mid = ll.size() / 2;
                        ll.add(mid, rnd.nextInt());
                        ll.remove(mid);
                    }
                }
                long t1 = System.nanoTime();
                if (run >= WARMUP_RUNS) {
                    times[run - WARMUP_RUNS] = t1 - t0;
                    last = ll.getMetrics().copy();
                }
            }
            long medianNs = median(times);
            rows.add(String.format("W3,%s,MyLinkedList,%d,%.3f,%d,%d,%d",
                    variant, n, medianNs / 1_000_000.0, last.getSteps(), last.getMoves(), last.getComparisons()));
            System.out.printf("  n=%d %s MyLinkedList  median=%.3f ms  %s%n", n, variant, medianNs / 1e6, last);
        }
    }

    private static void runW4(int n, List<String> rows) {
        Random rnd = new Random(SEED);
        int[] data = new int[n];
        for (int i = 0; i < n; i++) data[i] = rnd.nextInt();

        long[] times = new long[MEASURE_RUNS];
        Metrics last = null;
        boolean sortedOk = true;
        for (int run = 0; run < WARMUP_RUNS + MEASURE_RUNS; run++) {
            MinHeap heap = new MinHeap(n);
            long t0 = System.nanoTime();
            for (int v : data) heap.insert(v);
            int prev = Integer.MIN_VALUE;
            for (int i = 0; i < n; i++) {
                int v = heap.extractMin();
                if (v < prev) sortedOk = false;
                prev = v;
            }
            long t1 = System.nanoTime();
            if (run >= WARMUP_RUNS) {
                times[run - WARMUP_RUNS] = t1 - t0;
                last = heap.getMetrics().copy();
            }
        }
        if (!sortedOk) {
            System.err.println("WARNING: extractMin order is not non-decreasing for n=" + n);
        }
        long medianNs = median(times);
        rows.add(String.format("W4,-,MinHeap,%d,%.3f,%d,%d,%d",
                n, medianNs / 1_000_000.0, last.getSteps(), last.getMoves(), last.getComparisons()));
        System.out.printf("  n=%d MinHeap  median=%.3f ms  %s%n", n, medianNs / 1e6, last);
    }

    private static long median(long[] arr) {
        long[] copy = Arrays.copyOf(arr, arr.length);
        Arrays.sort(copy);
        return copy[copy.length / 2];
    }
}