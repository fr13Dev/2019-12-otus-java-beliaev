package ru.otus.annotations.testnb.runner;

import ru.otus.annotations.testnb.AfterEach;
import ru.otus.annotations.testnb.BeforeEach;
import ru.otus.annotations.testnb.Skip;
import ru.otus.annotations.testnb.Test;
import ru.otus.annotations.testnb.exception.TestFail;
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

        int amountOfSucceedTests = 0;
        if (testMethods != null) {
            for (Method testMethod : testMethods) {
                if (skipMethods.contains(testMethod)) {
                    System.out.println(testMethod.getName() + " - was skipped.");
                } else {
                    final T obj = reflection.getNewInstance();
                    boolean isDone = false;
                    try {
                        invokeMethod(reflection, initMethod, obj);
                        invokeMethod(reflection, testMethod, obj);
                        System.out.println(testMethod.getName() + " - done.");
                        isDone = true;
                    } catch (TestFail e) {
                        System.out.println(testMethod.getName() + e.getMessage());
                    } finally {
                        try {
                            invokeMethod(reflection, closeMethod, obj);
                        } catch (TestFail e) {
                            System.out.println(e.getMessage());
                            isDone = false;
                        }
                    }
                    if (isDone) {
                        amountOfSucceedTests++;
                    }
                }
            }
            System.out.println(
                    String.format(
                            "Total %d tests were ran, of which %d were skipped, %d were failed, %d were done.",
                            testMethods.size(), skipMethods.size(), (testMethods.size() - skipMethods.size()) - amountOfSucceedTests, amountOfSucceedTests));
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
        if (methods == null || methods.size() == 0) {
            return null;
        } else if (methods.size() > 1) {
            throw new IllegalStateException("It isn't possible to use annotations BeforeEach/AfterEach for more than one method.");
        } else {
            return methods.stream().findFirst().get();
        }
    }

    private static <T> void invokeMethod(Reflection<T> rfl, Method method, T obj) throws TestFail {
        if (method != null) {
            rfl.invokeMethod(obj, method);
        }
    }
}
