package ru.otus.annotations.testnb.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Reflection<T> {
    private final Map<Class<? extends Annotation>, Set<Method>> annotatedMethods = new HashMap<>();
    private final Class<T> clazz;

    public Reflection(Class<T> clazz) {
        this.clazz = clazz;
        findAnnotatedMethods();
    }

    public T getNewInstance() {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException();
        }
    }

    public boolean invokeMethod(T obj, Method method) {
        System.out.print(method.getName());
        try {
            method.invoke(obj);
            System.out.println(" - done.");
            return true;
        } catch (IllegalAccessException | InvocationTargetException e) {
            System.out.println(" - fail, reason: " + e.getCause().getMessage() + ".");
            return false;
        }
    }

    public Set<Method> getAnnotatedMethods(Class<? extends Annotation> clazz) {
        return annotatedMethods.get(clazz);
    }

    private void findAnnotatedMethods() {
        final Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            final Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                Set<Method> set = annotatedMethods.get(annotation.annotationType());
                if (set == null) {
                    set = new HashSet<>();
                }
                set.add(method);
                annotatedMethods.put(annotation.annotationType(), set);
            }
        }
    }
}
