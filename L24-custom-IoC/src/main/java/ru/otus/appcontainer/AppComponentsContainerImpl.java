package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponentsContainer;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AppComponentsContainerImpl implements AppComponentsContainer {
    private final Map<String, Object> container = new HashMap<>();
    private final Map<Class<?>, Reflection> reflections = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        reflections.put(initialConfigClass, new Reflection(initialConfigClass));
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        for (Class<?> configClass : initialConfigClasses) {
            reflections.put(configClass, new Reflection(configClass));
        }
        var configClasses = Reflection.getSortedConfigClasses(initialConfigClasses);
        for (Class<?> configClass : configClasses) {
            processConfig(configClass);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) container.get(Reflection.getComponentName(componentClass));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        return (C) container.get(componentName);
    }

    private void processConfig(Class<?> configClass) {
        var reflection = reflections.get(configClass);
        var methods = reflection.getSortedAnnotatedMethods();
        for (Method method : methods) {
            var args = reflection.getMethodArgs(method, this);
            var bean = reflection.invokeMethod(method, args);
            var interfaces = reflection.getObjectInterfaces(bean);
            for (AnnotatedType intrface : interfaces) {
                container.put(Reflection.getComponentName(intrface), bean);
            }
            container.put(Reflection.getComponentName(bean), bean);
            container.put(reflection.getAnnotatedMethodName(method), bean);
        }
    }
}
