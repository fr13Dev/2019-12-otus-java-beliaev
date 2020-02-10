package ru.otus.annotations.testnb.runner;

import ru.otus.annotations.testnb.AfterEach;
import ru.otus.annotations.testnb.BeforeEach;
import ru.otus.annotations.testnb.Skip;
import ru.otus.annotations.testnb.Test;
import ru.otus.annotations.testnb.exception.CloseFail;
import ru.otus.annotations.testnb.exception.InitFail;
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
                    System.out.print(testMethod.getName());
                    System.out.println(" - was skipped.");
                } else {
                    final T obj = reflection.getNewInstance();
                    invokeInitMethod(reflection, initMethod, closeMethod, obj);
                    if (invokeTestMethod(reflection, testMethod, closeMethod, obj)) {
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

    private static <T> void invokeInitMethod(Reflection<T> rfl, Method method, Method closeMethod, T obj) {
        if (method != null) {
            boolean isDone = false;
            try {
                rfl.invokeMethod(obj, method);
                isDone = true;
            } catch (TestFail e) {
                throw new InitFail();
            } finally {
                if (!isDone) {
                    invokeCloseMethod(rfl, closeMethod, obj);
                }
            }
        }
    }

    private static <T> void invokeCloseMethod(Reflection<T> rfl, Method method, T obj) {
        if (method != null) {
            try {
                rfl.invokeMethod(obj, method);
            } catch (TestFail e) {
                throw new CloseFail();
            }
        }
    }

    private static <T> boolean invokeTestMethod(Reflection<T> rfl, Method method, Method closeMethod, T obj) {
        System.out.print(method.getName());
        boolean isDone = false;
        try {
            rfl.invokeMethod(obj, method);
            System.out.println(" - done.");
            isDone = true;
        } catch (TestFail e) {
            System.out.println(e.getMessage());
        } finally {
            invokeCloseMethod(rfl, closeMethod, obj);
        }
        return isDone;
    }
}
