public class MinHeap {
    private int[] heap;
    private int size;
    private long comparisons;

    public MinHeap() {
        heap = new int[10];
        size = 0;
        comparisons = 0;
    }

    public int size() {
        return size;
    }

    public long getComparisons() {
        return comparisons;
    }

    public void resetComparisons() {
        comparisons = 0;
    }

    private void ensureCapacity() {
        if (size == heap.length) {
            int[] newHeap = new int[heap.length * 2];
            System.arraycopy(heap, 0, newHeap, 0, heap.length);
            heap = newHeap;
        }
    }

    public void insert(int x) {
        ensureCapacity();
        heap[size] = x;
        int current = size;
        size++;

        while (current > 0) {
            int parent = (current - 1) / 2;
            comparisons++;
            if (heap[parent] <= heap[current]) {
                break;
            }
            swap(parent, current);
            current = parent;
        }
    }

    public int extractMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        int result = heap[0];
        heap[0] = heap[size - 1];
        size--;

        int current = 0;
        while (true) {
            int left = current * 2 + 1;
            int right = current * 2 + 2;

            if (left >= size) {
                break;
            }

            int smallest = left;
            if (right < size) {
                comparisons++;
                if (heap[right] < heap[left]) {
                    smallest = right;
                }
            }

            comparisons++;
            if (heap[current] <= heap[smallest]) {
                break;
            }

            swap(current, smallest);
            current = smallest;
        }
        return result;
    }

    private void swap(int a, int b) {
        int temp = heap[a];
        heap[a] = heap[b];
        heap[b] = temp;
    }

    public boolean isValidHeap() {
        for (int i = 0; i < size; i++) {
            int left = i * 2 + 1;
            int right = i * 2 + 2;
            if (left < size && heap[i] > heap[left]) return false;
            if (right < size && heap[i] > heap[right]) return false;
        }
        return true;
    }
}