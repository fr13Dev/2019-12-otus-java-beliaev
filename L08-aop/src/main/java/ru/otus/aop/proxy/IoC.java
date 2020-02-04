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
            if (annotatedMethods.contains(getShortUniqueMethodName(method))) {
                System.out.println("executed method: " + method.getName() + ", params: " + getParams(args));
            }
            return method.invoke(obj, args);
        }

        private void findAnnotatedMethods() {
            final Class<?> clazz = obj.getClass();
            final Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                final var annotation = method.getAnnotation(Log.class);
                if (annotation != null) {
                    annotatedMethods.add(getShortUniqueMethodName(method));
                }
            }
        }

        private String getParams(Object[] args) {
            if (args == null) {
                return "no";
            } else {
                final StringBuilder builder = new StringBuilder();
                for (Object arg : args) {
                    builder.append(arg.toString());
                    builder.append(", ");
                }
                builder.replace(
                        builder.lastIndexOf(", "),
                        builder.length(),
                        "");
                return builder.toString();
            }
        }

        private String getShortUniqueMethodName(Method method) {
            return method.toGenericString().replaceAll(".+?(?=" + method.getName() + ")", "");
        }
    }
}
