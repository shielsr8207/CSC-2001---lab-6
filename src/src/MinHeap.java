import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * A min-heap of ints stored in an array, as presented in lecture.
 *
 * The heap is a complete binary tree kept in heap[1] .. heap[size];
 * heap[0] is never used. For the node at index i:
 *     left child  = 2 * i
 *     right child = 2 * i + 1
 *     parent      = i / 2
 *
 * Heap-order property: every parent is <= its children, so the
 * minimum is always at heap[1]. Duplicate values are allowed.
 *
 * Do not rename the class, the public methods, or change their
 * parameters/return types. You may add private helper methods.
 */
public class MinHeap {

    /** Number of values a new heap can hold before it has to grow. */
    public static final int DEFAULT_CAPACITY = 10;

    private int[] heap;   // heap[0] is unused; the root is heap[1]
    private int size;     // values live in heap[1] ... heap[size]

    /** Creates an empty heap with room for DEFAULT_CAPACITY values. */
    // Input: nothing
    // Output: a new empty MinHeap of capacity 10
    // ex: MinHeap heap = new MinHeap();
    // size = 0 (zero active elements), {0,0,0,0,0,0,0,0,0,0}
    public MinHeap() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Creates an empty heap with room for capacity values before it grows.
     * (The array needs capacity + 1 slots, since index 0 is unused.)
     * throws IllegalArgumentException if capacity is less than 1
     */
    // Input: capacity: int, the number of slots in the array
    // Output: a new empty MinHeap with capacity + 1 array slots
    // Example: MinHeap heqp = new MinHeap(5);
    // size = 0, heap.length = 5 , {0 , 0, 0 , 0 , 0}
    // new MinHeap(0) = throws IllegalArgumentException
    public MinHeap(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Illegal Capacity");
        }
        heap = new int[capacity + 1];
        size = 0;
    }

    /**
     * Builds a heap from values bottom-up in O(n): copies them into
     * heap[1] .. heap[n] in the given order, then calls buildHeap().
     * values itself must not be changed. (Make the array exactly big
     * enough: n + 1 slots, but at least 2.)
     * throws IllegalArgumentException if values is null
     */
    // Input: values: int[], array of non null values to be put in the heap
    // Output: a new MinHeap holding every value in values in minheap order
    // Example: MinHeap heap = new MinHeap(new int[]{9, 4, 7, 1, 5});
    // h.toArray() = {1, 4, 7, 9, 5}, size = 5
    // new MinHeap(null) = exception
    public MinHeap(int[] values) {
        if (values == null) {
            throw new IllegalArgumentException("input is null");
        }
        heap = new int[Math.max(values.length + 1 , 2)];
        for (int i = 0; i < values.length; i++) {
            heap[i+1] = values[i];
        }
        size = values.length;
        buildHeap();
    }

    /**
     * Adds value to the heap: puts it in the next open spot (index size + 1),
     * then percolates it up until its parent is no larger.
     * Grows the array first if it is full.
     */
    // Input: value: int, value to add
    // Output: nothing (void); the heap now also holds value size increases
    // Example: heap.toArray() = {3,5,8,10,7,12,9}
    // hepa.insert(4);
    // heap.toArray() = {3,4,8,5,7,12,9,10}, size= 8
    public void insert(int value) {
        if (size == heap.length - 1) {
            grow();
        }
        heap[size + 1] = value;
        size++;
        percolateUp(size);
    }

    /**
     * Removes and returns the smallest value: the last leaf moves to the
     * root, then percolates down until it is no larger than its children.
     * throws NoSuchElementException if the heap is empty
     */
    // Input: nothing
    // Output: int, the smallest value (removed from the heap)
    // Example: heap.toArray() = {3,5,8,10,7,12,9}
    // heap.removeMin() = 3,  heap.toArray() = {5,7,8,10,9,12}
    // new MinHeap().removeMin() = exception
    public int removeMin() {
        if (size == 0) {
            throw new NoSuchElementException("MinHeap is empty");
        }
        int min = heap[1];
        heap[1] = heap[size];
        size--;
        percolateDown(1);
        return min;
    }

    /**
     * Returns the smallest value without removing it.
     * throws NoSuchElementException if the heap is empty
     */
    // Input: nothing
    // Output: int, the smallest value heap isn't affected
    // Example: heap.toArray() = {3, 5, 8, 10, 7, 12, 9}
    // heap.peek() = 3, size= 7
    // new MinHeap().peek() = exception
    public int peek() {
        if (size == 0) {
            throw new NoSuchElementException("MinHeap is empty");
        }
        return heap[1];
    }

    /**
     * Returns the number of values in the heap.
     */
    // Input: nothing
    // Output: int, how many active values in the array/how many values the heap holds
    // Example: heap.toArray() = {3,5,8}
    // h.size() = 3
    public int size() {
        return size;
    }

    /**
     * Reports whether the heap has no values.
     * return true if the heap is empty
     */
    // Input: nothing
    // Output: boolean, true if size is 0
    // Example: new MinHeap().isEmpty() = true
    // after heap.insert(5); heap.isEmpty() = false
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the heap's values in array order (heap[1] ... heap[size]) as a
     * new array of length size(). Changing the returned array must not
     * change the heap.
     * Example: after inserting 3, 5, 8, 10, 7, 12, 9 this returns
     * {3, 5, 8, 10, 7, 12, 9}.
     * (The insert order matches the heap array order in this example)
     */
    // Input: nothing
    // Output: int[], a new copy of heap[1] -> heap[size]
    // Example: after inserting 3,5,8,10,7,12,9
    // h.toArray() = {3,5,8,10,7,12,9}
    // new MinHeap().toArray() = {}
    public int[] toArray() {
        int[] array = new int[size];
        for (int i = 1; i < size + 1; i++) {
            array[i - 1] = heap[i];
        }
        return array;
    }

    /**
     * Moves the value at index i up while it is smaller than its parent.
     * (Stop when it reaches the root or its parent is <= it.)
     */
    // Input: i: int, the index of the value to move up
    // Output: nothing (void); heap order is restored above index i
    // Example: heap = {3, 5, 8, 10, 7, 12, 9, 4}
    // percolateUp(8): 4 swaps with 10, then with 5, stops below 3
    // heap = {3, 4, 8, 5, 7, 12, 9, 10}
    private void percolateUp(int i) {
        while (i/2 > 0) {
            int parent = i / 2;
            if (heap[parent] > heap[i]) {
                swap(parent , i);
            }
            i = parent;
        }
    }

    /**
     * Moves the value at index i down, always swapping with the smaller
     * child (the LEFT child if both children are equal), until it is no
     * larger than its children or reaches a leaf.
     */
    // Input: i: int, the index of the value to move down
    // Output: nothing (void); heap order is restored below index i
    // Example: heap = {9, 5, 8, 10, 7, 12}
    // percolateDown(1): 9 swaps with 5, then with 7, stops at a leaf
    // heap = {5, 7, 8, 10, 9, 12}
    private void percolateDown(int i) {
        while (2 * i <= size) {
            int left = 2 * i;
            int right = 2 * i + 1;
            if (heap[left] < heap[i] && (right > size || heap[right] >= heap[left])) {
                swap(i, left);
                i = left;
            }else if (right <= size && heap[right] < heap[i]){
                swap(i, right);
                i =  right;
            } else {
                break;
            }
        }
    }

    /**
     * Exchanges heap[a] and heap[b].
     * a and b are the indices of the values to swap
     */
    // Input: a: int, b: int, the two indices to exchange
    // Output: nothing (void); the values at a and b trade places
    // Example: heap = {3, 5, 8}
    // swap(1, 3); heap= {8, 5, 3}
    private void swap(int a, int b) {
        int temp = heap[a];
        heap[a] = heap[b];
        heap[b] = temp;
    }

    /** Doubles the array's capacity, keeping every value at the same index. */
    // Input: nothing
    // Output: nothing (void); heap becomes an array twice as long
    // Example: h = new MinHeap(1) (array length 2), h.insert(5);
    // grow(); array length = 4, heap[1] = 5 (same index)
    private void grow() {
        heap = Arrays.copyOf(heap, heap.length * 2);
    }

    /**
     * Bottom up heap construction.
     * Restores heap order over heap[1] .. heap[size] by percolating down
     * each non-leaf node, starting at the last one (index size / 2) and
     * working back to the root (index 1).
     */
    // Input: nothing (works on heap[1] -> heap[size])
    // Output: nothing (void); heap[1] -> heap[size] in the heap order
    // Example: heap = {9, 4, 7, 1, 5}
    // buildHeap(); heap = {1, 4, 7, 9, 5}
    private void buildHeap() {
        for (int i = size / 2; i >= 1 ; i-- ){
            percolateDown(i);
        }

    }

    /**
     * Returns a new array holding values in ascending order: builds a
     * MinHeap from values with the MinHeap(int[]) constructor, then calls
     * removeMin() repeatedly. values itself must not be changed.
     * throws IllegalArgumentException if values is null
     */
    // Input: values: int[], the values to sort (not changed)
    // Output: int[], a new array of the same values in ascending order
    // Example: heapSort(new int[]{4, -2, 4, 0}) = {-2, 0, 4, 4}
    // heapSort(new int[]{}) = {}
    // heapSort(null) = exception
    public static int[] heapSort(int[] values) {
        MinHeap minHeap = new MinHeap(values);
        int[] sortedValues = new int[values.length];
        for (int i = 0; i < values.length ; i++) {
            sortedValues[i] = minHeap.removeMin();
        }
        return sortedValues;
    }
}