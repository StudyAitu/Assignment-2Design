import java.io.*;
import java.util.*;

public class Benchmark {
    static final int[] SIZES = {100, 1000, 10000, 100000};
    static final int REPEATS = 5;
    static final int GET_OPS = 10000;
    static final int SEARCH_OPS = 1000;
    static final int UPDATE_OPS = 1000;
    static final long SEED = 42L;

    public static void main(String[] args) throws Exception {
        new File("results/tables").mkdirs();
        workload1();
        workload2();
        workload3();
        workload4();
        System.out.println("Benchmark completed. CSV files are in results/tables/");
    }

    static long avg(long[] values) {
        long sum = 0;
        for (long v : values) sum += v;
        return sum / values.length;
    }

    static int[] randomData(int n, Random r) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = r.nextInt();
        return a;
    }

    static void workload1() throws Exception {
        try (PrintWriter out = new PrintWriter("results/tables/workload1.csv")) {
            out.println("n,structure,average_ns,accesses,theoretical");
            for (int n : SIZES) {
                int[] data = randomData(n, new Random(SEED));
                int[] indices = new int[GET_OPS];
                Random r = new Random(SEED);
                for (int i = 0; i < GET_OPS; i++) indices[i] = r.nextInt(n);

                for (String type : new String[]{"DynamicArray","LinkedList"}) {
                    long[] times = new long[REPEATS];
                    long totalAccesses = 0;
                    for (int rep = 0; rep < REPEATS; rep++) {
                        if (type.equals("DynamicArray")) {
                            DynamicArray<Integer> s = new DynamicArray<>();
                            for (int x : data) s.add(x);
                            s.resetOperationsCount();
                            long start = System.nanoTime();
                            for (int idx : indices) s.get(idx);
                            times[rep] = System.nanoTime() - start;
                            if (rep == 0) totalAccesses = s.getOperationsCount();
                        } else {
                            LinkedList<Integer> s = new LinkedList<>();
                            for (int x : data) s.add(x);
                            s.resetOperationsCount();
                            long start = System.nanoTime();
                            for (int idx : indices) s.get(idx);
                            times[rep] = System.nanoTime() - start;
                            if (rep == 0) totalAccesses = s.getOperationsCount();
                        }
                    }
                    String complexity = type.equals("DynamicArray") ? "Theta(1)" : "Theta(n)";
                    out.println(n + "," + type + "," + avg(times) + "," + totalAccesses + "," + complexity);
                }
            }
        }
    }

    static void workload2() throws Exception {
        try (PrintWriter out = new PrintWriter("results/tables/workload2.csv")) {
            out.println("n,structure,average_ns,comparisons,theoretical");
            for (int n : SIZES) {
                int[] data = randomData(n, new Random(SEED));
                int[] queries = new int[SEARCH_OPS];
                Random r = new Random(SEED);
                for (int i = 0; i < SEARCH_OPS; i++) queries[i] = r.nextInt();

                for (String type : new String[]{"DynamicArray","LinkedList"}) {
                    long[] times = new long[REPEATS];
                    long totalComparisons = 0;
                    for (int rep = 0; rep < REPEATS; rep++) {
                        long start;
                        if (type.equals("DynamicArray")) {
                            DynamicArray<Integer> s = new DynamicArray<>();
                            for (int x : data) s.add(x);
                            s.resetOperationsCount();
                            start = System.nanoTime();
                            for (int q : queries) s.contains(q);
                            times[rep] = System.nanoTime() - start;
                            if (rep == 0) totalComparisons = s.getOperationsCount();
                        } else {
                            LinkedList<Integer> s = new LinkedList<>();
                            for (int x : data) s.add(x);
                            s.resetOperationsCount();
                            start = System.nanoTime();
                            for (int q : queries) s.contains(q);
                            times[rep] = System.nanoTime() - start;
                            if (rep == 0) totalComparisons = s.getOperationsCount();
                        }
                    }
                    out.println(n + "," + type + "," + avg(times) + "," + totalComparisons + ",Theta(n)");
                }
            }
        }
    }

    static void workload3() throws Exception {
        try (PrintWriter out = new PrintWriter("results/tables/workload3.csv")) {
            out.println("n,structure,position,operation,average_ns,movements,theoretical");
            for (int n : SIZES) {
                int[] data = randomData(n, new Random(SEED));
                for (String type : new String[]{"DynamicArray","LinkedList"}) {
                    for (String position : new String[]{"beginning","middle"}) {
                        for (String operation : new String[]{"insert","remove"}) {
                            long[] times = new long[REPEATS];
                            long totalMovements = 0;

                            for (int rep = 0; rep < REPEATS; rep++) {
                                long start = 0;
                                if (type.equals("DynamicArray")) {
                                    DynamicArray<Integer> s = new DynamicArray<>();
                                    for (int x : data) s.add(x);
                                    s.resetOperationsCount();

                                    start = System.nanoTime();
                                    for (int k = 0; k < UPDATE_OPS; k++) {
                                        int currentSize = s.size();
                                        if (currentSize == 0) break;
                                        int index = position.equals("beginning") ? 0 : currentSize / 2;
                                        if (operation.equals("insert")) {
                                            s.add(index, k);
                                        } else {
                                            s.remove(index);
                                        }
                                    }
                                    times[rep] = System.nanoTime() - start;
                                    if (rep == 0) totalMovements = s.getOperationsCount();
                                } else {
                                    LinkedList<Integer> s = new LinkedList<>();
                                    for (int x : data) s.add(x);
                                    s.resetOperationsCount();

                                    start = System.nanoTime();
                                    for (int k = 0; k < UPDATE_OPS; k++) {
                                        int currentSize = s.size();
                                        if (currentSize == 0) break;
                                        int index = position.equals("beginning") ? 0 : currentSize / 2;
                                        if (operation.equals("insert")) {
                                            s.add(index, k);
                                        } else {
                                            s.remove(index);
                                        }
                                    }
                                    times[rep] = System.nanoTime() - start;
                                    if (rep == 0) totalMovements = s.getOperationsCount();
                                }
                            }
                            out.println(n + "," + type + "," + position + "," + operation + "," +
                                    avg(times) + "," + totalMovements + ",Theta(n)");
                        }
                    }
                }
            }
        }
    }

    static void workload4() throws Exception {
        try (PrintWriter out = new PrintWriter("results/tables/workload4.csv")) {
            out.println("n,insert_average_ns,extract_average_ns,insert_comparisons,extract_comparisons,sorted");
            for (int n : SIZES) {
                int[] data = randomData(n, new Random(SEED));
                long[] insertTimes = new long[REPEATS];
                long[] extractTimes = new long[REPEATS];
                long insertComparisons = 0, extractComparisons = 0;
                boolean sorted = true;

                for (int rep = 0; rep < REPEATS; rep++) {
                    MinHeap h = new MinHeap();
                    h.resetComparisons();
                    long start = System.nanoTime();
                    for (int x : data) h.insert(x);
                    insertTimes[rep] = System.nanoTime() - start;
                    insertComparisons += h.getComparisons();

                    h.resetComparisons();
                    start = System.nanoTime();
                    int previous = Integer.MIN_VALUE;
                    for (int i = 0; i < n; i++) {
                        int x = h.extractMin();
                        if (x < previous) sorted = false;
                        previous = x;
                    }
                    extractTimes[rep] = System.nanoTime() - start;
                    extractComparisons += h.getComparisons();
                }

                out.println(n + "," + avg(insertTimes) + "," + avg(extractTimes) + "," +
                        insertComparisons / REPEATS + "," + extractComparisons / REPEATS + "," + sorted);
            }
        }
    }
}


