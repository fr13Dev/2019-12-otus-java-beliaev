package ru.otus.annotations.testnb.runner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Runner {
    private static Map<String, List<Method>> annotatedMethods = new HashMap<>();

    public static void run(String className) {
        try {
            run(Class.forName(className));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException();
        }
    }

    public static <T> void run(Class<T> clazz) {
        // TODO: use a different class to get annotatedMethods
        // TODO: solve the issue about wrong qnt of the annotations
        final Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            final Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                List<Method> list = annotatedMethods.get(annotation.getClass().getSimpleName());
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(method);
                // TODO: use unique name
                annotatedMethods.put(annotation.getClass().getName(), list);
            }
        }
    }

    public static void run(Object obj) {
        run(obj.getClass());
    }
}
