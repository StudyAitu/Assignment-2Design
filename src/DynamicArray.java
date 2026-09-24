public class DynamicArray<T> {
    private Object[] data;
    private int size;
    private long operationsCount; // Счетчик сдвигов / доступов / сравнений

    public DynamicArray() {
        data = new Object[10];
        size = 0;
        operationsCount = 0;
    }

    public int size() {
        return size;
    }

    public long getOperationsCount() {
        return operationsCount;
    }

    public void resetOperationsCount() {
        operationsCount = 0;
    }

    private void ensureCapacity() {
        if (size == data.length) {
            Object[] newData = new Object[data.length * 2];
            System.arraycopy(data, 0, newData, 0, data.length);
            data = newData;
        }
    }

    public void add(T x) {
        ensureCapacity();
        data[size] = x;
        size++;
    }

    public void add(int index, T x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        ensureCapacity();
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
            operationsCount++; // считаем перемещения элементов при сдвиге
        }
        data[index] = x;
        size++;
    }

    @SuppressWarnings("unchecked")
    public T remove(int index) {
        checkIndex(index);
        T removed = (T) data[index];
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
            operationsCount++; // считаем перемещения при удалении
        }
        data[size - 1] = null;
        size--;
        return removed;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkIndex(index);
        operationsCount++; // доступ к элементу
        return (T) data[index];
    }

    public boolean contains(T x) {
        for (int i = 0; i < size; i++) {
            operationsCount++; // сравнение при поиске
            if (data[i].equals(x)) {
                return true;
            }
        }
        return false;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
}