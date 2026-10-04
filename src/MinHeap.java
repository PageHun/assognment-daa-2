public class MinHeap {
    private int[] data;
    private int size;

    public MinHeap() {
        this(16);
    }

    public MinHeap(int capacity) {
        if (capacity < 1) capacity = 1;
        this.data = new int[capacity];
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void insert(int x) {
        ensureCapacity(size + 1);
        data[size] = x;
        size++;
        bubbleUp(size - 1);
    }

    public int peekMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        return data[0];
    }

    public int extractMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        int min = data[0];
        size--;
        if (size > 0) {
            data[0] = data[size];
            bubbleDown(0);
        }
        return min;
    }

    public void buildHeap(int[] array) {
        if (array == null) {
            throw new IllegalArgumentException("array is null");
        }
        size = array.length;
        data = new int[Math.max(size, 16)];
        System.arraycopy(array, 0, data, 0, size);
        for (int i = (size / 2) - 1; i >= 0; i--) {
            bubbleDown(i);
        }
    }

    private void bubbleUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (data[index] >= data[parent]) {
                break;
            }
            int temp = data[index];
            data[index] = data[parent];
            data[parent] = temp;
            index = parent;
        }
    }

    private void bubbleDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < size) {
                if (data[left] < data[smallest]) {
                    smallest = left;
                }
            }
            if (right < size) {
                if (data[right] < data[smallest]) {
                    smallest = right;
                }
            }
            if (smallest == index) {
                break;
            }
            int temp = data[index];
            data[index] = data[smallest];
            data[smallest] = temp;
            index = smallest;
        }
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity <= data.length) return;
        int newCap = data.length * 2;
        if (newCap < minCapacity) newCap = minCapacity;
        int[] newData = new int[newCap];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }
        data = newData;
    }

    public boolean isHeap() {
        for (int i = 0; i < size; i++) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            if (left < size && data[i] > data[left]) return false;
            if (right < size && data[i] > data[right]) return false;
        }
        return true;
    }

    public int[] toArray() {
        int[] copy = new int[size];
        System.arraycopy(data, 0, copy, 0, size);
        return copy;
    }
}

