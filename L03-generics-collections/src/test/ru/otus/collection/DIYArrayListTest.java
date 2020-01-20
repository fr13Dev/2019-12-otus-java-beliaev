package ru.otus.collection;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class DIYArrayListTest {
    private final static int LIST_SIZE = 100;
    List<Integer> list = new DIYArrayList<>();

    @Test
    public void addCollectionToEmpty() {
        Collections.addAll(list, 1, 2, 3, 4, 5);
        assertEquals(5, list.size());
        assertEquals(4, (long) list.get(3));
    }

    @Test
    public void addCollectionToNonEmpty() {
        populate(list, LIST_SIZE);
        Collections.addAll(list, 1, 2, 3, 4, -1);
        assertEquals(105, list.size());
        assertEquals(-1, (long) list.get(104));
    }

    @Test
    public void copyCollection() {
        List<Integer> temp = new DIYArrayList<>(LIST_SIZE);
        populate(temp, LIST_SIZE);
        populate(list, LIST_SIZE, 0);
        Collections.copy(list, temp);
        assertEquals(temp.get(LIST_SIZE - 1), list.get(LIST_SIZE - 1));
    }

    @Test
    public void sortCollection() {
        populate(list, LIST_SIZE);
        Collections.sort(list, Comparator.reverseOrder());
        assertEquals(LIST_SIZE - 1, (long) list.get(0));
    }

    @Test
    public void getFirstItem() {
        populate(list, LIST_SIZE);
        final int actualValue = list.get(0);
        final int expectedValue = 0;
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void getLastItem() {
        populate(list, LIST_SIZE);
        final int actualItem = list.get(LIST_SIZE - 1);
        assertEquals(LIST_SIZE - 1, actualItem);
    }

    @Test
    public void addAllToSmallerEmpty() {
        List<Integer> temp = new DIYArrayList<>();
        populate(temp, LIST_SIZE);
        list.addAll(temp);
        assertEquals(temp.size(), list.size());
        assertEquals(LIST_SIZE - 1, (long) list.get(LIST_SIZE - 1));
    }

    @Test
    public void addAllToBiggerEmpty() {
        List<Integer> temp = new DIYArrayList<>();
        final int index = 2;
        populate(temp, index);
        list.addAll(temp);
        assertEquals(temp.size(), list.size());
        assertEquals(0, (long) list.get(0));
    }

    @Test
    public void addAllToSmallerNonEmpty() {
        List<Integer> temp = new DIYArrayList<>();
        populate(temp, LIST_SIZE);
        final int index = 2;
        populate(list, index);
        list.addAll(index, temp);
        assertEquals(temp.size() + index, list.size());
        assertEquals(10, (long) list.get(12));
    }

    @Test
    public void addAllToBiggerNonEmpty() {
        List<Integer> temp = new DIYArrayList<>();
        final int index = 2;
        populate(temp, index);
        populate(list, LIST_SIZE);
        list.addAll(index, temp);
        assertEquals(LIST_SIZE, list.size());
        assertEquals((long) temp.get(index - 1), (long) list.get(index + 1));
    }

    @Test
    public void addAllOverride() {
        List<Integer> temp = new DIYArrayList<>();
        populate(temp, LIST_SIZE);
        final byte index = 2;
        populate(list, index);
        final byte insertIndex = 1;
        list.addAll(insertIndex, temp);
        assertEquals(temp.size() + insertIndex, list.size());
        assertEquals(10, (long) list.get(11));
    }

    @Test
    public void forwardDirectionOfListIterator() {
        populate(list, LIST_SIZE);
        final ListIterator<Integer> iterator = list.listIterator();
        int counter = 0;
        while (iterator.hasNext()) {
            final Integer i = iterator.next();
            assertEquals(list.get(i), i);
            counter++;
        }
        assertEquals(LIST_SIZE, counter);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void invokeIndexOutOfBoundsException() {
        final Integer i = list.get(0);
    }

    @Test
    public void clear() {
        populate(list, LIST_SIZE);
        list.clear();
        Assert.assertTrue(list.isEmpty());
    }

    @Test
    public void removeFirstByIndex() {
        populate(list, LIST_SIZE);
        final Integer i = list.remove(0);
        assertEquals(LIST_SIZE - 1, list.size());
        assertEquals((long) i, 0);
        assertEquals(1, (long) list.get(0));
    }

    @Test
    public void removeLastByIndex() {
        populate(list, LIST_SIZE);
        final Integer i = list.remove(LIST_SIZE - 1);
        assertEquals(LIST_SIZE - 1, list.size());
        assertEquals((long) i, LIST_SIZE - 1);
        assertEquals(LIST_SIZE - 2, (long) list.get(LIST_SIZE - 2));
    }

    @Test
    public void removeMiddleByIndex() {
        populate(list, LIST_SIZE);
        final Integer i = list.remove(LIST_SIZE / 2);
        assertEquals(LIST_SIZE - 1, list.size());
        assertEquals((long) i, LIST_SIZE / 2);
        assertEquals(LIST_SIZE - 1, (long) list.get(LIST_SIZE - 2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removeFirstByObject() {
        List<String> l = new DIYArrayList<>(1);
        l.add("a");
        l.remove("a");
        assertEquals(0, list.size());
        final Integer i = list.get(0);
    }

    private void populate(Collection<Integer> c, int itemCount) {
        for (int i = 0; i < itemCount; i++) {
            c.add(i);
        }
    }

    private void populate(Collection<Integer> c, int itemCount, int value) {
        for (int i = 0; i < itemCount; i++) {
            c.add(value);
        }
    }
}