package ru.otus.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class AppComponentsContainerImpl implements AppComponentsContainer {
    private final Map<String, Object> container = new HashMap<>();
    private final Map<Class<?>, Reflection> reflections = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        reflections.put(initialConfigClass, new Reflection(initialConfigClass));
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        processConfigClasses(initialConfigClasses);
    }

    public AppComponentsContainerImpl(String packageName) {
        var reflections = new Reflections(packageName, new TypeAnnotationsScanner());
        var classes = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class, true);
        processConfigClasses(classes.toArray(new Class[classes.size()]));
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

    private void processConfigClasses(Class<?>[] initialConfigClasses) {
        for (Class<?> configClass : initialConfigClasses) {
            reflections.put(configClass, new Reflection(configClass));
        }
        var configClasses = Reflection.getSortedConfigClasses(initialConfigClasses);
        for (Class<?> configClass : configClasses) {
            processConfig(configClass);
        }
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
