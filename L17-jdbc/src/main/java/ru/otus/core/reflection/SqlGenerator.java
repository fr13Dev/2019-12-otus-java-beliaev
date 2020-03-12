package ru.otus.core.reflection;

import java.util.stream.Collectors;

public class SqlGenerator<T> {
    private final Reflection<T> reflection;

    public SqlGenerator(Class<T> clazz) {
        reflection = new Reflection<>(clazz);
    }

    public String getInsertQuery() {
        return "";
    }

    public String getSelectQuery() {
        return "";
    }

    private String getTableName() {
        return reflection.getObjectName().toLowerCase();
    }

    private String getFields() {
        return reflection.getFieldsNames().collect(Collectors.joining(","));
    }
}
