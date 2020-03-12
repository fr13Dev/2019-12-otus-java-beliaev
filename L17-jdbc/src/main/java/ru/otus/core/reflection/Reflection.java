package ru.otus.core.reflection;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Stream;

public class Reflection<T> {
    private final Class<T> clazz;

    public Reflection(Class<T> clazz) {
        this.clazz = clazz;
    }

    public String getObjectName() {
        return clazz.getSimpleName();
    }

    public Stream<String> getFieldsNames() {
        return Arrays.stream(clazz.getDeclaredFields()).map(Field::getName);
    }
}
