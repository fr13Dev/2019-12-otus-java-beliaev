package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Reflection {
    private final Class<?> clazz;
    private final Object instance;

    public Reflection(Class<?> clazz) {
        this.clazz = clazz;
        this.instance = createInstance();
    }

    public List<Method> getSortedAnnotatedMethods() {
        return Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order()))
                .collect(Collectors.toList());
    }

    public static List<Class<?>> getSortedConfigClasses(Class<?>... classes) {
        return Arrays.stream(classes)
                .peek(Reflection::checkConfigClass)
                .sorted(Comparator.comparingInt(c -> c.getAnnotation(AppComponentsContainerConfig.class).order()))
                .collect(Collectors.toList());
    }

    public static String getComponentName(Class<?> component) {
        return component.getTypeName();
    }

    public static String getComponentName(Object object) {
        return object.getClass().getTypeName();
    }

    public static String getComponentName(AnnotatedType type) {
        return type.getType().getTypeName();
    }

    public Object invokeMethod(Method method, Object... args) {
        try {
            return method.invoke(instance, args);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public String getAnnotatedMethodName(Method method) {
        return method.getAnnotation(AppComponent.class).name();
    }

    public AnnotatedType[] getObjectInterfaces(Object object) {
        return object.getClass().getAnnotatedInterfaces();
    }

    public Object[] getMethodArgs(Method method, AppComponentsContainer container) {
        var args = new Object[method.getParameterCount()];
        var parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            final Class<?> type = parameters[i].getType();
            args[i] = container.getAppComponent(Reflection.getComponentName(type));
        }
        return args;
    }

    private Object createInstance() {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }
}
