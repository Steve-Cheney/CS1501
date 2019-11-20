/*
   Stephen Cheney || 4275535

   IndexMinPQ is the min and max priority queue object. This is indexable, so you can use the index to return the values
 */

import java.util.NoSuchElementException;

public class IndexMinPQ<Key extends Comparable<Key> > {
private int capacity;
private int size;
public Key[] keys;     //given an index, contains the key of that index
public int[] pq;       //binary heap using 1-based indexing
private int[] qp;       //contains the index of the pq array that a specified INDEX resides
private boolean minMax; // true means it is kept as a min PQ, false, it is a max PQ

@SuppressWarnings("unchecked")
public IndexMinPQ(int capacity, boolean minMax) {
        this.minMax = minMax;
        this.capacity = capacity;
        size = 0;
        keys = (Key[]) new Comparable[capacity + 1];
        pq = new int[capacity + 1];
        qp = new int[capacity + 1];
        for(int i = 0; i <= capacity; i++) {
                qp[i] = -1;
        }
}


public boolean isEmpty(){
        if (size == 0)
                return true;
        else
                return false;
}

public void insert(int i, Key key) {
        if(contains(i)) {
                throw new IllegalArgumentException("Index " + i + " already exists in pq.");
        }
        size++;
        pq[size] = i;
        qp[i] = size;
        keys[i] = key;
        swim(size);
}

public boolean contains(int i) {
        if(i > capacity || i < 0) {
                return false;
        }
        return qp[i] != -1;
}

public int minIndex() {
        return pq[1];
}

public Key minKey() {
        return keys[minIndex()];
}

public void deleteMin() {
        int min = pq[1];
        exch(1, size--);
        sink(1);
        assert min == pq[size+1];
        qp[min] = -1;         // delete
        keys[min] = null;         // to help with garbage collection
        pq[size+1] = -1;
}

public Key delete(int i) {
        Key key = keyOf(i);
        int index = qp[i];
        exch(index, size--);
        swim(index);
        sink(index);
        keys[i] = null;
        qp[i] = -1;
        return key;
}

public Key keyOf(int i) {
        if (i < 0 || i >= capacity) throw new IllegalArgumentException();
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        else return keys[i];
}

public void changeKey(int i, Key key) {
        if (i < 0 || i >= capacity) throw new IllegalArgumentException();
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        keys[i] = key;
        swim(qp[i]);
        sink(qp[i]);
}

/**
 * Change the key associated with index {@code i} to the specified value.
 *
 * @param  i the index of the key to change
 * @param  key change the key associated with index {@code i} to this key
 * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
 * @deprecated Replaced by {@code changeKey(int, Key)}.
 */
@Deprecated
public void change(int i, Key key) {
        changeKey(i, key);
}

/***************************************************************************
* General helper functions.
***************************************************************************/
private boolean greater(int i, int j) {
        if(minMax)
                // min PQ
                return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
        else
                // max PQ
                return keys[pq[i]].compareTo(keys[pq[j]]) < 0;
}

private void exch(int i, int j) {
        int swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
        qp[pq[i]] = i;
        qp[pq[j]] = j;
}


/***************************************************************************
* Heap helper functions.
***************************************************************************/
private void swim(int k) {
        while (k > 1 && greater(k/2, k)) {
                exch(k, k/2);
                k = k/2;
        }
}

private void sink(int k) {
        while (2*k <= size) {
                int j = 2*k;
                if (j < size && greater(j, j+1)) j++;
                if (!greater(k, j)) break;
                exch(k, j);
                k = j;
        }
}
}
