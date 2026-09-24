public class Tests {
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    public static void main(String[] args) {
        testDynamicArray();
        testLinkedList();
        testHeap();
        System.out.println("All tests passed.");
    }

    private static void testDynamicArray() {
        DynamicArray<Integer> a = new DynamicArray<>();
        check(a.size() == 0, "empty array");
        a.add(10); a.add(20); a.add(1, 15);
        check(a.get(1) == 15, "insert/get");
        check(a.contains(20), "contains");
        check(a.remove(1) == 15, "remove");
        boolean thrown = false;
        try { a.get(99); } catch (IndexOutOfBoundsException e) { thrown = true; }
        check(thrown, "invalid index");
    }

    private static void testLinkedList() {
        LinkedList<Integer> l = new LinkedList<>();
        l.add(1); l.add(2); l.add(1, 5);
        check(l.get(1) == 5, "list insert/get");
        check(l.contains(2), "list contains");
        check(l.remove(1) == 5, "list remove");
    }

    private static void testHeap() {
        MinHeap h = new MinHeap();
        int[] values = {7, 3, 9, 1, 5, 1, 8};
        for (int x : values) {
            h.insert(x);
            check(h.isValidHeap(), "heap property after insertion");
        }
        int previous = Integer.MIN_VALUE;
        while (h.size() > 0) {
            int x = h.extractMin();
            check(x >= previous, "non-decreasing extraction");
            previous = x;
            check(h.isValidHeap(), "heap property after extraction");
        }
    }
}