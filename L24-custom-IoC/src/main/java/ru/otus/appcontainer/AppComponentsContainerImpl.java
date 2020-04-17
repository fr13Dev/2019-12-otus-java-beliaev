package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponentsContainer;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class AppComponentsContainerImpl implements AppComponentsContainer {
    private static final String CLASS_NAME_POSTFIX = ".class";
    private final Map<String, Object> container = new HashMap<>();
    private final Map<Class<?>, Reflection> reflections = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        reflections.put(initialConfigClass, new Reflection(initialConfigClass));
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        processConfigClasses(initialConfigClasses);
    }

    public AppComponentsContainerImpl(String packageName) throws IOException, ClassNotFoundException {
        var classes = getPackageClasses(packageName);
        processConfigClasses(classes);
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

    private Class<?>[] getPackageClasses(String packageName) throws IOException, ClassNotFoundException {
        var classLoader = Thread.currentThread().getContextClassLoader();
        var path = packageName.replace(".", "/");
        var resources = classLoader.getResources(path);
        var files = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            var resource = resources.nextElement();
            files.add(new File(resource.getFile()));
        }
        var classes = new ArrayList<Class<?>>();
        for (File file : files) {
            classes.addAll(findClasses(file, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
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

    private List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        var classes = new ArrayList<Class<?>>();
        if (!directory.exists()) {
            return classes;
        }
        var files = directory.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + '.' + file.getName()));
            } else if (file.getName().endsWith(CLASS_NAME_POSTFIX)) {
                final String className = packageName + '.' + file.getName().replace(CLASS_NAME_POSTFIX, "");
                final Class<?> clazz = Class.forName(className);
                classes.add(clazz);
            }
        }
        return classes;
    }
}
