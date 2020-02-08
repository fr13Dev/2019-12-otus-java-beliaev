package ru.otus.annotations.testnb.runner;

import ru.otus.annotations.testnb.AfterEach;
import ru.otus.annotations.testnb.BeforeEach;
import ru.otus.annotations.testnb.Skip;
import ru.otus.annotations.testnb.Test;
import ru.otus.annotations.testnb.reflection.Reflection;

import java.lang.reflect.Method;
import java.util.Set;

public class Runner {

    public static <T> void run(Class<T> clazz) {
        final Reflection<T> reflection = new Reflection<>(clazz);
        final Set<Method> initMethods = reflection.getAnnotatedMethods(BeforeEach.class);
        final Set<Method> closeMethods = reflection.getAnnotatedMethods(AfterEach.class);
        final Set<Method> testMethods = reflection.getAnnotatedMethods(Test.class);
        final Set<Method> skipMethods = reflection.getAnnotatedMethods(Skip.class);

        final Method initMethod = getFirst(initMethods);
        final Method closeMethod = getFirst(closeMethods);

        int done = 0;
        if (testMethods != null) {
            for (Method method : testMethods) {
                if (skipMethods.contains(method)) {
                    System.out.print(method.getName());
                    System.out.println(" - was skipped.");
                } else {
                    final T obj = reflection.getNewInstance();
                    if (initMethod != null) {
                        reflection.invokeMethod(obj, initMethod);
                    }
                    if (reflection.invokeMethod(obj, method)) {
                        done++;
                    }
                    if (closeMethod != null) {
                        reflection.invokeMethod(obj, closeMethod);
                    }
                }
                System.out.println();
            }
            System.out.println(
                    String.format(
                            "Total %d tests were ran, of which %d were skipped, %d were failed, %d were done.",
                            testMethods.size(), skipMethods.size(), (testMethods.size() - skipMethods.size()) - done, done));
        }
    }

    public static void run(String className) {
        try {
            run(Class.forName(className));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException();
        }
    }

    @SuppressWarnings("unused")
    public static void run(Object obj) {
        run(obj.getClass());
    }

    private static Method getFirst(Set<Method> methods) {
        if (methods == null
                || methods.size() == 0) {
            return null;
        } else if (methods.size() > 1) {
            throw new IllegalStateException(
                    "It isn't possible to use annotations BeforeEach/AfterEach for more than one method.");
        } else {
            return methods.stream()
                    .findFirst()
                    .get();
        }
    }
}
