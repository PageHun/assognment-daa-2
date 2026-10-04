package com.daa;

public class DynamicArray {
    private int[] data;
    private int size;
    private final Metrics metrics;

    public DynamicArray() {
        this(16);
    }

    public DynamicArray(int capacity) {
        if (capacity < 1) capacity = 1;
        this.data = new int[capacity];
        this.size = 0;
        this.metrics = new Metrics();
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public void resetMetrics() {
        metrics.reset();
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(int x) {
        ensureCapacity(size + 1);
        data[size] = x;
        metrics.addMove();
        size++;
    }

    public void add(int index, int x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        ensureCapacity(size + 1);
        for (int i = size; i > index; i--) {
            metrics.addStep();
            data[i] = data[i - 1];
            metrics.addMove();
        }
        data[index] = x;
        metrics.addMove();
        size++;
    }

    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        metrics.addStep();
        int removed = data[index];
        for (int i = index; i < size - 1; i++) {
            metrics.addStep(); // read data[i+1]
            data[i] = data[i + 1];
            metrics.addMove();
        }
        size--;
        return removed;
    }

    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        metrics.addStep();
        return data[index];
    }

    public boolean contains(int x) {
        for (int i = 0; i < size; i++) {
            metrics.addStep();
            metrics.addComparison();
            if (data[i] == x) {
                return true;
            }
        }
        return false;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity <= data.length) {
            return;
        }
        int newCap = data.length * 2;
        if (newCap < minCapacity) {
            newCap = minCapacity;
        }
        int[] newData = new int[newCap];
        for (int i = 0; i < size; i++) {
            metrics.addStep();
            newData[i] = data[i];
            metrics.addMove();
        }
        data = newData;
    }

    public int[] toArray() {
        int[] copy = new int[size];
        System.arraycopy(data, 0, copy, 0, size);
        return copy;
    }

    int[] getData() {
        return data;
    }
}
