package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AppComponentsContainerImpl implements AppComponentsContainer {
    private final Map<String, Object> container = new HashMap<>();
    private final Reflection reflection;

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        this.reflection = new Reflection(initialConfigClass);
        processConfig(initialConfigClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) container.get(reflection.getComponentName(componentClass));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        return (C) container.get(componentName);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        var methods = reflection.getAnnotatedMethods();
        for (Method method : methods) {
            var args = reflection.getMethodArgs(method, this);
            var bean = reflection.invokeMethod(method, args);
            var interfaces = reflection.getObjectInterfaces(bean);
            for (AnnotatedType intrface : interfaces) {
                container.put(reflection.getComponentName(intrface), bean);
            }
            container.put(reflection.getComponentName(bean), bean);
            container.put(reflection.getAnnotatedMethodName(method), bean);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }
}
