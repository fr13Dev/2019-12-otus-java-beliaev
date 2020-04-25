package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponentsContainer;

import java.lang.reflect.Method;
import java.util.*;

@SuppressWarnings("unused")
public class AppComponentsContainerImpl implements AppComponentsContainer {
    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> componentsByName = new HashMap<>();
    private final Map<Class<?>, Reflection> reflections = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        reflections.put(initialConfigClass, new Reflection(initialConfigClass));
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        var configClassesWrapper = new ConfigClassesWrapper(initialConfigClasses);
        processConfigClasses(configClassesWrapper.getConfigClasses());
    }

    public AppComponentsContainerImpl(String packageName) {
        var configClassesWrapper = new ConfigClassesWrapper(packageName);
        processConfigClasses(configClassesWrapper.getConfigClasses());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(Class<C> componentClass) {
        C bean = null;
        for (Object component : appComponents) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                bean = (C) component;
                break;
            }
        }
        if (bean == null) {
            throw new NoSuchElementException(String.format("No any bean for %s", componentClass.getName()));
        }
        return bean;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        return (C) componentsByName.get(componentName);
    }

    private void processConfigClasses(List<Class<?>> configClasses) {
        for (Class<?> configClass : configClasses) {
            reflections.put(configClass, new Reflection(configClass));
            processConfig(configClass);
        }
    }

    private void processConfig(Class<?> configClass) {
        var reflection = reflections.get(configClass);
        var methods = reflection.getSortedAnnotatedMethods();
        for (Method method : methods) {
            var args = reflection.getMethodArgs(method, this::getAppComponent);
            var bean = reflection.invokeMethod(method, args);
            appComponents.add(bean);
            componentsByName.put(reflection.getAnnotatedMethodName(method), bean);
        }
    }
}
