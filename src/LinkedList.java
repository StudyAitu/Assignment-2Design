public class LinkedList<T> {
    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    private Node<T> head;
    private int size;
    private long operationsCount;

    public int size() {
        return size;
    }

    public long getOperationsCount() {
        return operationsCount;
    }

    public void resetOperationsCount() {
        operationsCount = 0;
    }

    public void add(T x) {
        if (head == null) {
            head = new Node<>(x);
        } else {
            Node<T> current = head;
            while (current.next != null) {
                current = current.next;
                operationsCount++;
            }
            current.next = new Node<>(x);
        }
        size++;
    }

    public void add(int index, T x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        if (index == 0) {
            Node<T> newNode = new Node<>(x);
            newNode.next = head;
            head = newNode;
            size++;
            return;
        }

        Node<T> current = head;
        for (int i = 0; i < index - 1; i++) {
            current = current.next;
            operationsCount++; // обход до нужного узла
        }

        Node<T> newNode = new Node<>(x);
        newNode.next = current.next;
        current.next = newNode;
        size++;
    }

    public T remove(int index) {
        checkIndex(index);

        if (index == 0) {
            T removed = head.data;
            head = head.next;
            size--;
            return removed;
        }

        Node<T> current = head;
        for (int i = 0; i < index - 1; i++) {
            current = current.next;
            operationsCount++;
        }

        T removed = current.next.data;
        current.next = current.next.next;
        size--;
        return removed;
    }

    public T get(int index) {
        checkIndex(index);
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
            operationsCount++; // шаги по ссылкам (доступы)
        }
        return current.data;
    }

    public boolean contains(T x) {
        Node<T> current = head;
        while (current != null) {
            operationsCount++; // сравнение
            if (current.data.equals(x)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
}