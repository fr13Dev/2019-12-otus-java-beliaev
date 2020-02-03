package ru.otus.aop.proxy;

import annotation.Log;
import stringpalindrome.StringPalindrome;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;

public class IoC {

    @SuppressWarnings("unchecked")
    public static <T> T getInstance(T obj) {
        final InvocationHandler handler = new CustomInvocationHandler<>(obj);
        return (T) Proxy.newProxyInstance(
                IoC.class.getClassLoader(),
                new Class<?>[]{StringPalindrome.class},
                handler);
    }

    private static class CustomInvocationHandler<T> implements InvocationHandler {
        private final T obj;
        private final Set<String> annotatedMethods = new HashSet<>();

        public CustomInvocationHandler(T obj) {
            this.obj = obj;
            findAnnotatedMethods();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (annotatedMethods.contains(method.getName())) {
                System.out.println("executed method: " + method.getName() + ", param: " + getFirstParam(args));
            }
            return method.invoke(obj, args);
        }

        private void findAnnotatedMethods() {
            final Class<?> clazz = obj.getClass();
            final Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                final var annotation = method.getAnnotation(Log.class);
                if (annotation != null) {
                    annotatedMethods.add(method.getName());
                }
            }
        }

        private String getFirstParam(Object[] args) {
            return (args != null && args.length == 1 ? args[0].toString() : "no");
        }
    }
}
