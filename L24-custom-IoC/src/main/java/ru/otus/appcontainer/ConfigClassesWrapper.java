package ru.otus.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigClassesWrapper {
    private final List<Class<?>> configClasses;

    public ConfigClassesWrapper(String packageName) {
        var reflections = new Reflections(packageName, new TypeAnnotationsScanner());
        var classes = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class, true);
        classes.forEach(this::checkConfigClass);
        this.configClasses = new ArrayList<>(classes);
    }

    public ConfigClassesWrapper(Class<?>... configClasses) {
        Arrays.stream(configClasses).forEach(this::checkConfigClass);
        this.configClasses = List.of(configClasses);
    }

    public List<Class<?>> getConfigClasses() {
        return configClasses.stream()
                .sorted(Comparator.comparingInt(c -> c.getAnnotation(AppComponentsContainerConfig.class).order()))
                .collect(Collectors.toUnmodifiableList());
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }
}
