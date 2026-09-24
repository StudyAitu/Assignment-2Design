# Assignment 2: Algorithmic Analysis, Correctness and Performance Trade-offs

## 1. Overview
The purpose of this laboratory work is the practical implementation, empirical benchmarking, and theoretical analysis of three fundamental data structures[cite: 1, 6]:
1. **Dynamic Array** — a data structure based on a contiguous block of memory with automatic scaling.
2. **Linked List** — a linear data structure consisting of nodes connected by pointers.
3. **Min-Heap** — a specialized array-based structure that maintains the heap property for efficient searching and extraction of the minimum element[cite: 1, 6].

The main objective of the work is not just to implement structures, but to prove their correctness using loop invariants, perform asymptotic analysis ($\mathcal{O}, \Omega, \Theta$), design controlled workloads, and compare the predicted theoretical complexity with actual measurement results[cite: 1, 6].

---

## 2. Complexity Analysis
Below is a summary table of asymptotic complexity for key operations of the implemented data structures[cite: 1, 6].

| Data Structure | Operation | Best Case | Average Case | Worst Case | Auxiliary Space |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Dynamic Array** | `get(index)` | $\Omega(1)$ | $\Theta(1)$ | $O(1)$ | $\Theta(1)$ |
| **Dynamic Array** | `contains(x)` | $\Omega(1)$ | $\Theta(n)$ | $O(n)$ | $\Theta(1)$ |
| **Dynamic Array** | `add(index, x)` | $\Omega(1)$ | $\Theta(n)$ | $O(n)$ | $\Theta(n)$ (amortized) |
| **Dynamic Array** | `remove(index)` | $\Omega(1)$ | $\Theta(n)$ | $O(n)$ | $\Theta(1)$ |
| **Linked List** | `get(index)` | $\Omega(1)$ | $\Theta(n)$ | $O(n)$ | $\Theta(1)$ |
| **Linked List** | `contains(x)` | $\Omega(1)$ | $\Theta(n)$ | $O(n)$ | $\Theta(1)$ |
| **Linked List** | `add(index, x)` | $\Omega(1)$ | $\Theta(n)$ | $O(n)$ | $\Theta(1)$ |
| **Linked List** | `remove(index)` | $\Omega(1)$ | $\Theta(n)$ | $O(n)$ | $\Theta(1)$ |
| **Min-Heap** | `insert(x)` | $\Omega(1)$ | $\Theta(\log n)$ | $O(\log n)$ | $\Theta(1)$ |
| **Min-Heap** | `peekMin()` | $\Omega(1)$ | $\Theta(1)$ | $O(1)$ | $\Theta(1)$ |
| **Min-Heap** | `extractMin()`| $\Omega(1)$ | $\Theta(\log n)$ | $O(\log n)$ | $\Theta(1)$ |

### Justification of Differences:
* **Index Access (`get`):** In a dynamic array, access occurs in $\Theta(1)$ time due to direct memory addressing by offset. In a linked list, there is no direct access, so a linear traversal of nodes from `head` to the target index is required ($\Theta(n)$)[cite: 1, 6].
* **Insertion at the Beginning / Middle:** For a dynamic array, insertion requires shifting all subsequent elements to the right ($O(n)$ movements). For a linked list, finding the position takes $O(n)$, but the pointer insertion itself is executed in $\Theta(1)$ time.

---

## 3. Correctness & Loop Invariants
To prove correctness, let's examine two non-trivial operations: searching for an element in a linked list (`LinkedList.contains`) and extracting the minimum in a heap (`MinHeap.extractMin`)[cite: 1, 6].

### Proof 1: `LinkedList.contains(T x)`
* **Loop Invariant:** At the start of each loop iteration, none of the nodes traversed from `head` up to the current node `current` contain a value equal to `x`.
* **Initialization:** Before the loop starts, `current = head`. No nodes have been checked yet, making the invariant trivially true.
* **Maintenance:** During the current iteration, `current.data` is checked. If `current.data.equals(x)`, the algorithm correctly returns `true`. If not, the pointer is shifted to `current = current.next`, expanding the checked region by one node which is guaranteed not to contain `x`. The invariant is preserved.
* **Termination:** The loop terminates when `current == null` (list exhausted) or the element is found. If `current == null`, this means all elements of the list were checked and none of them matched `x`, therefore returning `false` is correct.

### Proof 2: `MinHeap.extractMin()`
* **Loop Invariant:** At each step of the sift-down process, the element at position `current` is greater than or equal to its parent (the heap property is maintained above), and the sub-heap below restores order after moving the last element to the root.
* **Initialization:** The root of the heap is replaced by the last element `heap[size]`, which may temporarily violate the heap property locally for the root and its children.
* **Maintenance:** The algorithm finds the smaller child and compares it with `current`. If the min-heap property is violated, the elements are swapped, and the `current` variable moves down to the child node. This reduces the number of order inversions in the heap.
* **Termination:** The loop terminates when the node has no children (`left >= size`) or the condition `heap[current] <= heap[smallest]` is met. At this point, the local min-heap property is restored for all nodes.

---

## 4. Experimental Setup
Experiments were conducted in a controlled environment with the following parameters[cite: 1, 6]:
* **Input Sizes ($n$):** 100, 1,000, 10,000, 100,000 elements[cite: 1, 6].
* **Number of Operations ($m$):** 
  * `GET_OPS` = 10,000 (Workload 1)
  * `SEARCH_OPS` = 1,000 (Workload 2)
  * `UPDATE_OPS` = 1,000 (Workload 3)
* **Number of Repetitions:** Each experiment was run 5 times to eliminate JVM overhead (JIT compilation, garbage collection), and the arithmetic mean time (`System.nanoTime()`) was recorded in the report[cite: 1, 6].
* **Random Number Generator:** A fixed seed of `Random(42)` was used for reproducibility of results[cite: 1, 6].

---

## 5. Experimental Results
Benchmark results are automatically saved in CSV format to the `results/tables/` folder[cite: 1, 2, 6]:
* `workload1.csv` — random access (`get`).
* `workload2.csv` — search (`contains`).
* `workload3.csv` — insertions and removals at the beginning and middle.
* `workload4.csv` — priority processing in Min-Heap.

Graphs showing the dependence of execution time and operations on size $n$ are saved in the `results/plots/` directory[cite: 1, 2, 6]:
* `results/plots/execution_time.png`
* `results/plots/operations.png`

---

## 6. Discussion
1. **Theory and Practice Alignment:** The results for `Workload 1` fully confirm the theoretical complexity: access time for `DynamicArray` remains constant $\Theta(1)$, while for `LinkedList` it grows linearly $\Theta(n)$[cite: 1, 6].
2. **Memory and Cache Overhead:** The dynamic array works significantly faster than the linked list even during search operations due to CPU cache locality, whereas `LinkedList` nodes are scattered throughout memory.
3. **Insertion at the Beginning:** For an array, inserting at index `0` requires moving all $n$ elements, which results in a significant increase in execution time at large scales.

---

## 7. Design Recommendations
* **When to use Dynamic Array:** Ideal for tasks requiring frequent index access (`get/set`) and appending elements to the end (e.g., object lists, data buffers)[cite: 1, 6].
* **When to use Linked List:** Useful for frequent insertions and deletions at arbitrary positions if the iterator is already at the target element, or when it is critical to avoid memory reallocation of the entire array[cite: 1, 6].
* **When to use Min-Heap:** Indispensable for implementing priority queues, shortest path algorithms (e.g., Dijkstra's), and sorting tasks[cite: 1, 6].

---

## 8. Conclusion
During the execution of this assignment, the dynamic array, linked list, and binary heap structures were successfully implemented and tested. Empirical tests confirmed the correctness of the theoretical analysis, and the developed benchmarking module clearly demonstrated the performance differences among various data structures under workload[cite: 1, 6].
