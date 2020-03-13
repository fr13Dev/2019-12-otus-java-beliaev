package ru.otus.core.sql;

import java.lang.reflect.Field;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SqlGenerator<T> {
    private final Reflection<T> reflection;

    public SqlGenerator(Class<T> clazz) {
        reflection = new Reflection<>(clazz);
    }

    public String getInsertQuery() {
        var builder = new StringBuilder();
        return builder.append("insert into ")
                .append(getTableName())
                .append("(")
                .append(concatFieldNames(reflection.getFieldNames().filter(getFilterByNonAnnotatedFields()), Field::getName))
                .append(") values(")
                .append(concatFieldNames(reflection.getFieldNames().filter(getFilterByNonAnnotatedFields()), f -> "?"))
                .append(")").toString();
    }

    public String getSelectQuery() {
        var builder = new StringBuilder();
        return builder.append("select ")
                .append(concatFieldNames(reflection.getFieldNames(), Field::getName))
                .append(" from ")
                .append(getTableName())
                .append(" where ")
                .append(concatFieldNames(reflection.getFieldNames().filter(getFilterByAnnotatedFields()), Field::getName))
                .append("= ?").toString();
    }

    private String getTableName() {
        return reflection.getObjectName().toLowerCase();
    }

    private String concatFieldNames(Stream<Field> stream, Function<? super Field, ? extends String> mapper) {
        return stream.map(mapper).collect(Collectors.joining(","));
    }

    private Predicate<? super Field> getFilterByNonAnnotatedFields() {
        return field -> !field.isAnnotationPresent(Id.class);
    }

    private Predicate<? super Field> getFilterByAnnotatedFields() {
        return field -> field.isAnnotationPresent(Id.class);
    }
}
