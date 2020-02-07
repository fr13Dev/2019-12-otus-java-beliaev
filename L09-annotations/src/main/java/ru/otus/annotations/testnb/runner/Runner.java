package ru.otus.annotations.testnb.runner;

import ru.otus.annotations.testnb.AfterEach;
import ru.otus.annotations.testnb.BeforeEach;
import ru.otus.annotations.testnb.Skip;
import ru.otus.annotations.testnb.Test;
import ru.otus.annotations.testnb.reflection.Reflection;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class Runner {
    private static final String BEFORE_EACH = BeforeEach.class.getSimpleName();
    private static final String AFTER_EACH = AfterEach.class.getSimpleName();
    private static final String TEST = Test.class.getSimpleName();
    private static final String SKIP = Skip.class.getSimpleName();

    public static <T> void run(Class<T> clazz) {
        final Reflection<T> reflection = new Reflection<>(clazz);
        final Map<String, Set<Method>> annotatedMethods = reflection.getAnnotatedMethods();

        final Set<Method> initMethods = annotatedMethods.get(BEFORE_EACH);
        final Set<Method> closeMethods = annotatedMethods.get(AFTER_EACH);
        final Set<Method> testMethods = annotatedMethods.get(TEST);
        final Set<Method> skipMethods = annotatedMethods.get(SKIP);

        final Method initMethod = getFirst(initMethods);
        final Method closeMethod = getFirst(closeMethods);

        int done = 0;
        int fail = 0;
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
                    } else {
                        fail++;
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
                            testMethods.size(), skipMethods.size(), fail, done));
        }
    }

    public static void run(String className) {
        try {
            run(Class.forName(className));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException();
        }
    }

    public static void run(Object obj) {
        run(obj.getClass());
    }

    private static Method getFirst(Set<Method> methods) {
        if (methods != null
                && methods.size() != 0) {
            return methods.stream()
                    .findFirst()
                    .get();
        } else {
            return null;
        }
    }
}
