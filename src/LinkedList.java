public class LinkedList {
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

    public LinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
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
        } else {
            node.prev = tail;
            tail.next = node;
            tail = node;
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
            if (head != null) {
                head.prev = node;
            }
            head = node;
            if (tail == null) {
                tail = node;
            }
            size++;
            return;
        }
        Node current = getNode(index); // already counts steps
        Node node = new Node(x);
        node.prev = current.prev;
        node.next = current;
        current.prev.next = node;
        current.prev = node;
        size++;
    }

    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node current = getNode(index);
        int value = current.value;

        if (current.prev != null) {
            current.prev.next = current.next;
        } else {
            head = current.next;
        }
        if (current.next != null) {
            current.next.prev = current.prev;
        } else {
            tail = current.prev;
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
        return n.value;
    }

    public boolean contains(int x) {
        Node current = head;
        while (current != null) {
            if (current.value == x) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    private Node getNode(int index) {
        Node current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
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