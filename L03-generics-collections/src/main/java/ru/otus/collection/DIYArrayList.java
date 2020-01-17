package ru.otus.collection;

import java.util.*;

public class DIYArrayList<T> implements List<T> {
    private static final int DEFAULT_INITIAL_CAPACITY = 10;
    private static final int MAX__CAPACITY = Integer.MAX_VALUE;
    private int currentCapacity;
    private int size;
    private T[] storage;

    @SuppressWarnings("unused")
    public DIYArrayList(int initialCapacity) {
        currentCapacity = initialCapacity;
        initStorage(currentCapacity);
    }

    public DIYArrayList() {
        currentCapacity = DEFAULT_INITIAL_CAPACITY;
        initStorage(currentCapacity);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean add(T t) {
        if (size == MAX__CAPACITY) {
            throw new OutOfMemoryError();
        }
        if (size == currentCapacity) {
            currentCapacity *= 2;
            T[] temp = (T[]) new Object[currentCapacity];
            System.arraycopy(storage, 0, temp, 0, size);
            storage = temp;
        }
        storage[size] = t;
        size++;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean addAll(int index, Collection<? extends T> c) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        if (c.size() + index < currentCapacity) {
            System.arraycopy(c.toArray(), 0, storage, index, c.size());
            if (index == 0) {
                size = c.size();
            }
        } else {
            currentCapacity = c.size() + index;
            Object[] temp = new Object[currentCapacity];
            if (index != 0) {
                System.arraycopy(storage, 0, temp, 0, index);
            }
            System.arraycopy(c.toArray(), 0, temp, index, c.size());
            storage = (T[]) temp;
            size = storage.length;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        addAll(0, c);
        return true;
    }

    @Override
    public T get(int index) {
        if (size == 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        return storage[index];
    }

    @Override
    public Iterator<T> iterator() {
        return new DIYIterator();
    }

    @Override
    public Object[] toArray() {
        final Object[] dest = new Object[size];
        System.arraycopy(storage, 0, dest, 0, size);
        return dest;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            storage[i] = null;
        }
        size = 0;
    }

    @Override
    public ListIterator<T> listIterator() {
        return new DIYListIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new DIYListIterator(index);
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        boolean founded = false;
        for (int i = 0; i < size; i++) {
            if (storage[i].equals(o)) {
                remove(i);
                founded = true;
                break;
            }
        }
        return founded;
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException();
        }
        final T i = storage[index];
        if (size == 1) {
            storage[index] = null;
        } else {
            System.arraycopy(storage, index + 1, storage, index, size - index - 1);
            storage[size] = null;
        }
        size--;
        return i;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T set(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    private void initStorage(int capacity) {
        storage = (T[]) new Object[capacity];
    }

    private class DIYIterator implements Iterator<T> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override
        public T next() {
            final T i = storage[currentIndex];
            currentIndex++;
            return i;
        }
    }

    private class DIYListIterator implements ListIterator<T> {
        private static final int CURSOR_FIRST_VALUE = -1;
        private int cursor;

        public DIYListIterator() {
            this(CURSOR_FIRST_VALUE);
        }

        public DIYListIterator(int index) {
            cursor = index;
        }

        @Override
        public boolean hasNext() {
            return cursor != size - 1;
        }

        @Override
        public T next() {
            if (cursor > size) {
                throw new NoSuchElementException("There is no next element.");
            }
            cursor++;
            return storage[cursor];
        }

        @Override
        public boolean hasPrevious() {
            return cursor != CURSOR_FIRST_VALUE;
        }

        @Override
        public T previous() {
            if (cursor == CURSOR_FIRST_VALUE) {
                throw new NoSuchElementException("There is no previous element.");
            }
            cursor--;
            return storage[cursor];
        }

        @Override
        public int nextIndex() {
            return cursor + 1;
        }

        @Override
        public int previousIndex() {
            return cursor == CURSOR_FIRST_VALUE ? CURSOR_FIRST_VALUE : cursor - 1;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(T t) {
            storage[cursor] = t;
        }

        @Override
        public void add(T t) {
            throw new UnsupportedOperationException();
        }
    }
}
