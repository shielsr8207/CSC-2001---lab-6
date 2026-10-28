import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for MinHeap.
 *
 * These starter tests (with these exact names) should all pass by the
 * time you're done. They are NOT enough on their own - add your own
 * tests (see the TODO list at the bottom and the lab handout).
 */
public class MinHeapTest {

    // ---- starter tests ----

    @Test
    void newHeapIsEmpty() {
        MinHeap h = new MinHeap();
        assertTrue(h.isEmpty());
        assertEquals(0, h.size());
        assertArrayEquals(new int[0], h.toArray());
    }

    @Test
    void peekOnEmptyHeapThrows() {
        assertThrows(NoSuchElementException.class, () -> new MinHeap().peek());
    }

    @Test
    void buildFromNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> new MinHeap(null));
    }

    @Test
    void insertOneValue() {
        MinHeap h = new MinHeap(1);
        h.insert(42);
        assertFalse(h.isEmpty());
        assertEquals(1, h.size());
        assertEquals(42, h.peek());
        assertArrayEquals(new int[]{42}, h.toArray());
    }

    // TODO: add your own tests. At a minimum, cover:
    //   - removeMin() on an empty heap throws NoSuchElementException
    @Test
    void removeOnEmptyHeap() {
        MinHeap h = new MinHeap(1);
        assertThrows(NoSuchElementException.class, () -> h.removeMin());
    }
    //   - new MinHeap(0) throws IllegalArgumentException
    @Test
    void newEmptyHeapIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new MinHeap(0));
    }
    //   - peek() does not remove the value
    @Test
    void peekDoesNotRemove(){
        MinHeap h = new MinHeap(new int[]{1,2,3,4,5});
        h.peek();
        assertEquals(h.peek(), 1);

    }
    //   - inserting a new minimum percolates all the way to the root
    @Test
    void insertMinimumPerculatesToRoot(){
        MinHeap h = new MinHeap(new int[]{2,3,4,5,6});
        h.insert(1);
        assertEquals(h.peek(), 1);
    }
    //   - removeMin() when both children are equal swaps with the LEFT child
    @Test
    void removeMinSwapsWithLeft(){
        MinHeap h = new MinHeap(new int[]{1,5,5,9});
        h.removeMin();
        assertArrayEquals(new int[]{5 , 9 , 5}, h.toArray());

    }
    //   - duplicate and negative values
    @Test
    void duplicateAndNegativeValues(){
        MinHeap h = new MinHeap();
        h.insert(-3);
        h.insert(4);
        h.insert(-3);
        h.insert(0);
        h.insert(4);
        h.insert(-7);
        assertArrayEquals(new int[]{-7, 0, -3, 4, 4, -3}, h.toArray());

        assertEquals(-7, h.removeMin());
        assertEquals(-3, h.removeMin());
        assertEquals(-3, h.removeMin());
        assertEquals(0, h.removeMin());
        assertEquals(4, h.removeMin());
        assertEquals(4, h.removeMin());
        assertTrue(h.isEmpty());

    }

    //   - inserting more values than the initial capacity (growing)
    @Test
    void testGrowth(){
        MinHeap h = new MinHeap(new int[]{1,2,3,4,5});
        h.insert(1);
        assertEquals(6, h.size());
        assertArrayEquals(new int[]{1, 2, 1, 4, 5, 3}, h.toArray());
    }
    //   - toArray() returns a copy (changing it does not change the heap)
    @Test
    void toArrayReturnsCopy(){
        MinHeap h = new MinHeap(new int[]{1,2,3,4,5});
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, h.toArray());
        int[] newArray = h.toArray();
        newArray[3] = 1;
        assertFalse(Arrays.equals(h.toArray(), newArray));
    }
    //   - many inserts followed by removeMin() return values in sorted order
    @Test
    void manyInsertsReturnsSortedOrder(){
        MinHeap h = new MinHeap();
        h.insert(2);
        h.insert(5);
        h.insert(4);
        h.insert(1);
        h.insert(6);
        h.insert(3);
        assertArrayEquals(new int[]{1 , 2 , 3 , 5 , 6 , 4}, h.toArray());

        assertEquals(1 ,h.removeMin());
        assertEquals(2 ,h.removeMin());
        assertEquals(3 ,h.removeMin());
        assertEquals(4 ,h.removeMin());
        assertEquals(5 ,h.removeMin());
        assertEquals(6, h.removeMin());

    }
    //   - new MinHeap(int[]): an example that comes out DIFFERENT from inserting
    //the same values one at a time, an empty array, one value, and that the
    //caller's array is not changed
    @Test
    void buildHeap(){
        MinHeap h = new MinHeap(new int[]{3 , 2 , 1});
        assertArrayEquals(new int[]{1 , 2, 3}, h.toArray());

        MinHeap h2 = new MinHeap();
        h2.insert(3);
        h2.insert(2);
        h2.insert(1);
        assertArrayEquals(new int[]{1, 3 , 2}, h2.toArray());

    }

    //   - a heap built from an array still supports insert (it must grow) and removeMin
    @Test
    void heapFromArraySupportsInsertAndRemoveMin() {
        MinHeap h = new MinHeap(new int[]{1, 2, 3, 4, 5});   // array is exactly full
        h.insert(1);                                         // must grow
        assertEquals(6, h.size());
        assertArrayEquals(new int[]{1, 2, 1, 4, 5, 3}, h.toArray());

        assertEquals(1, h.removeMin());
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, h.toArray());
        assertEquals(1, h.removeMin());
        assertEquals(2, h.removeMin());
        assertEquals(3, h.removeMin());
        assertEquals(4, h.removeMin());
        assertEquals(5, h.removeMin());
        assertTrue(h.isEmpty());
    }
    //   - heapSort: empty array, duplicates, negatives, input not changed
    @Test
    void heapSortEmptyArray() {
        assertArrayEquals(new int[]{}, MinHeap.heapSort(new int[]{}));
    }

    @Test
    void heapSortDuplicatesAndNegatives() {
        int[] input = {5, -1, 3, -1, 0, 5, -8};
        assertArrayEquals(new int[]{-8, -1, -1, 0, 3, 5, 5}, MinHeap.heapSort(input));
    }

    @Test
    void heapSortDoesNotChangeInput() {
        int[] input = {5, -1, 3, -1, 0, 5, -8};
        MinHeap.heapSort(input);
        assertArrayEquals(new int[]{5, -1, 3, -1, 0, 5, -8}, input);
    }
}
