package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AppComponentsContainerImpl implements AppComponentsContainer {
    private final Map<String, Object> appComponentsByName = new HashMap<>();
    private final Reflection reflection;

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        this.reflection = new Reflection(initialConfigClass);
        processConfig(initialConfigClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) appComponentsByName.get(reflection.getComponentName(componentClass));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName.toUpperCase());
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        var methods = reflection.getAnnotatedMethods();
        for (Method method : methods) {
            var args = getMethodArgs(method);
            var bean = reflection.invokeMethod(method, args);
            var name = reflection.getAnnotatedMethodName(method);
            appComponentsByName.put(name.toUpperCase(), bean);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private Object[] getMethodArgs(Method method) {
        var args = new Object[method.getParameterCount()];
        var parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            final Class<?> type = parameters[i].getType();
            args[i] = appComponentsByName.get(reflection.getComponentName(type));
        }
        return args;
    }
}
