public class MyLinkedList {
    private static class Node {
        int value;
        Node prev;
        Node next;

        Node(int value) {
            this.value = value;
        }
    }

    private Node head;
    private Node tail;
    private int size;
    private final Metrics metrics;

    public MyLinkedList() {
        this.head = null;
        this.tail = null;
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
        Node node = new Node(x);
        if (tail == null) {
            head = tail = node;
            metrics.addMove();
            metrics.addMove();
        } else {
            node.prev = tail;
            metrics.addMove();
            tail.next = node;
            metrics.addMove();
            tail = node;
            metrics.addMove();
        }
        size++;
    }

    public void add(int index, int x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (index == size) {
            add(x);
            return;
        }
        if (index == 0) {
            Node node = new Node(x);
            node.next = head;
            metrics.addMove();
            if (head != null) {
                head.prev = node;
                metrics.addMove();
            }
            head = node;
            metrics.addMove();
            if (tail == null) {
                tail = node;
                metrics.addMove();
            }
            size++;
            return;
        }
        Node current = getNode(index);
        Node node = new Node(x);
        node.prev = current.prev;
        metrics.addMove();
        node.next = current;
        metrics.addMove();
        current.prev.next = node;
        metrics.addMove();
        current.prev = node;
        metrics.addMove();
        size++;
    }

    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node current = getNode(index);
        int value = current.value;
        metrics.addStep();

        if (current.prev != null) {
            current.prev.next = current.next;
            metrics.addMove();
        } else {
            head = current.next;
            metrics.addMove();
        }
        if (current.next != null) {
            current.next.prev = current.prev;
            metrics.addMove();
        } else {
            tail = current.prev;
            metrics.addMove();
        }
        current.prev = null;
        current.next = null;
        size--;
        return value;
    }

    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node n = getNode(index);
        metrics.addStep();
        return n.value;
    }

    public boolean contains(int x) {
        Node current = head;
        while (current != null) {
            metrics.addStep();
            metrics.addComparison();
            if (current.value == x) {
                return true;
            }
            current = current.next;
            metrics.addStep();
        }
        return false;
    }

    private Node getNode(int index) {
        Node current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                metrics.addStep();
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                metrics.addStep();
                current = current.prev;
            }
        }
        return current;
    }

    public int[] toArray() {
        int[] arr = new int[size];
        Node current = head;
        int i = 0;
        while (current != null) {
            arr[i++] = current.value;
            current = current.next;
        }
        return arr;
    }
}