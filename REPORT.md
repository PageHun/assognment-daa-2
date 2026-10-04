# Assignment 2 Report – Data Structures

## 1. Complexity Table

| Structure     | Operation          | Best          | Average       | Worst         | Aux. space | Justification |
|---------------|--------------------|---------------|---------------|---------------|------------|---------------|
| DynamicArray  | add(x)             | Θ(1)          | Θ(1) amort.   | Θ(n)          | Θ(1)       | Amortized O(1) because of 2× growth; worst case is a full resize (copy n elements). |
| DynamicArray  | add(index, x)      | Θ(1)          | Θ(n)          | Θ(n)          | Θ(1)       | Shift of (size−index) elements; best when index = size. |
| DynamicArray  | remove(index)      | Θ(1)          | Θ(n)          | Θ(n)          | Θ(1)       | Shift of (size−index−1) elements. |
| DynamicArray  | get(index)         | Θ(1)          | Θ(1)          | Θ(1)          | Θ(1)       | Direct array access. |
| DynamicArray  | contains(x)        | Θ(1)          | Θ(n)          | Θ(n)          | Θ(1)       | Linear scan; best when found at first position. |
| MyLinkedList  | add(x)             | Θ(1)          | Θ(1)          | Θ(1)          | Θ(1)       | Append to tail (doubly linked). |
| MyLinkedList  | add(index, x)      | Θ(1)          | Θ(n)          | Θ(n)          | Θ(1)       | Traversal to index (from nearer end) + O(1) pointer updates. |
| MyLinkedList  | remove(index)      | Θ(1)          | Θ(n)          | Θ(n)          | Θ(1)       | Same traversal cost; pointer updates O(1). |
| MyLinkedList  | get(index)         | Θ(1)          | Θ(n)          | Θ(n)          | Θ(1)       | Must walk the chain; best for index 0 or size−1. |
| MyLinkedList  | contains(x)        | Θ(1)          | Θ(n)          | Θ(n)          | Θ(1)       | Linear scan from head. |
| MinHeap       | insert(x)          | Θ(1)          | Θ(log n)      | Θ(log n)      | Θ(1)       | Bubble-up height is O(log n); best when no swaps needed. |
| MinHeap       | peekMin()          | Θ(1)          | Θ(1)          | Θ(1)          | Θ(1)       | Root is at index 0. |
| MinHeap       | extractMin()       | Θ(1)          | Θ(log n)      | Θ(log n)      | Θ(1)       | Bubble-down height O(log n). |
| MinHeap       | buildHeap(arr)     | Θ(n)          | Θ(n)          | Θ(n)          | Θ(1)       | Floyd’s algorithm: sum of heights is < 2n. |

All structures store only primitive `int`; no boxing.

## 2. Loop Invariant Proofs

### Proof 1 – `DynamicArray.contains(int x)`

```java
for (int i = 0; i < size; i++) {
    if (data[i] == x) return true;
}
return false;
```

**Invariant**  
Before the iteration with index `i` (0 ≤ i ≤ size):  
*No element in the sub-array `data[0 … i−1]` equals `x`.*

**Initialization**  
Before the first iteration `i = 0`. The range `data[0 … −1]` is empty, therefore the invariant holds vacuously.

**Maintenance**  
Assume the invariant holds at the beginning of an iteration with index `i`.  
The body compares `data[i]` with `x`.
- If equal, the method returns `true` (correctness is immediate).
- If not equal, after the iteration `i` becomes `i+1` and the range `data[0 … i]` still contains no occurrence of `x`. Thus the invariant is re-established for the next iteration.

**Termination**  
The loop ends when `i = size`. By the invariant the whole array `data[0 … size−1]` contains no `x`, therefore the method correctly returns `false`.

**Conclusion**  
The invariant together with the exit condition proves that `contains` returns `true` if and only if `x` occurs at least once in the array.

---

### Proof 2 – `MinHeap.bubbleDown(int index)` (core of `extractMin`)

```java
while (true) {
    int left = 2*index+1, right = 2*index+2;
    int smallest = index;
    if (smallest == index) break;
    index = smallest;
}
```

**Invariant**  
Before every iteration:  
*The sub-tree rooted at every node **except** the current `index` already satisfies the min-heap property*  
(i.e. `parent ≤ both children`). In particular, both children of `index` (if they exist) are roots of valid heaps.

**Initialization**  
Right after `extractMin` places the last element at the root, every node other than the root already belonged to a valid heap (the original heap property). The two children of the root are themselves roots of valid heaps. Hence the invariant holds for `index = 0`.

**Maintenance**  
Assume the invariant holds. The algorithm selects the smallest among `index`, left and right.
- If `index` is already the smallest, the loop terminates.
- Otherwise a swap is performed with the smaller child. After the swap the heap property is restored between the new parent and its two children. The only node that may now violate the property is the child that received the larger value; that child becomes the new `index`. All other sub-trees remain valid. Therefore the invariant is preserved.

**Termination**  
The loop ends when `index` is a leaf or is smaller than both its children. By the invariant the whole tree now satisfies the min-heap property.

**Conclusion**  
After `bubbleDown` finishes, the array is again a valid min-heap, which is exactly the post-condition required by `extractMin`.

## 3. Plots

Recommended charts (one per workload):

- **W1** – Time (ms) vs *n* for DynamicArray vs MyLinkedList; Steps vs *n*.
- **W2** – Time vs *n*; Comparisons vs *n*.
- **W3-head** & **W3-middle** – Time vs *n*; Moves vs *n*.
- **W4** – Time vs *n*; Comparisons vs *n* (and optionally buildHeap vs n inserts).

## 4. Discussion (cache locality & pointer chasing)

DynamicArray stores all elements contiguously in a single primitive array.  
A `get(i)` therefore costs exactly one array load; the CPU can prefetch whole cache lines (typically 64 bytes ≈ 16 ints). Even a sequential scan (`contains` or a simple for-loop) benefits from spatial locality and hardware prefetchers, which is why DynamicArray is dramatically faster than MyLinkedList for random access and for linear search, even when the asymptotic number of steps looks similar.

MyLinkedList, on the other hand, stores each integer inside a separate `Node` object. Each node carries an object header (≈12–16 bytes), two pointers (`prev`, `next`) and the integer itself; after alignment a node occupies 24–32 bytes. Consecutive logical elements are almost never consecutive in memory, so every step is a pointer chase that almost always misses L1/L2 cache. In addition the garbage collector must track millions of small objects, which adds further overhead. Consequently, even when both structures perform roughly the same number of abstract “steps”, the wall-clock time of the linked list is several times higher.

For **insert/remove at the head** the linked list wins: only a constant number of pointer assignments are needed, whereas the array must shift every element. For **middle** operations both structures are Θ(n), but the constant factors still favour the array for moderate sizes because of better cache behaviour; only for very large *n* and frequent middle updates does the list become preferable.

The MinHeap is the structure of choice whenever the workload is priority-based (repeated extract-min). Its O(log n) height guarantees that the total cost of *n* inserts + *n* extract-mins is O(n log n), and the contiguous array layout again gives excellent cache performance. When the whole data set is known in advance, Floyd’s bottom-up `buildHeap` reduces the construction cost from O(n log n) to O(n) and roughly halves the number of comparisons, which is visible both in the metrics and in the measured time.

**Summary of practical advice**

- Random access / sequential scan → DynamicArray
- Frequent insert/delete at both ends → MyLinkedList (or a deque)
- Priority queue / online scheduling → MinHeap
- Bulk construction of a heap → `buildHeap` (Floyd)