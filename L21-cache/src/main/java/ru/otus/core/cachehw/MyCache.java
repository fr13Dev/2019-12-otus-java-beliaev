package ru.otus.core.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(MyCache.class);
    private final Map<K, V> storage = new WeakHashMap<>();
    private final Set<HwListener<K, V>> listeners = Collections.newSetFromMap(new WeakHashMap<>());

    @Override
    public void put(K key, V value) {
        storage.put(key, value);
        listeners.forEach(i -> i.notify(key, value, "put"));
        logger.debug("object is cached: {}", value);
    }

    @Override
    public void remove(K key) {
        final V removedObject = storage.remove(key);
        listeners.forEach(i -> i.notify(key, removedObject, "remove"));
        logger.debug("object is removed: {}", removedObject);
    }

    @Override
    public V get(K key) {
        final V object = storage.get(key);
        listeners.forEach(i -> i.notify(key, object, "get"));
        logger.debug("object is retrieved: {}", object);
        return object;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
        logger.debug("listener is added: {}", listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
        logger.debug("listener is removed: {}", listener);
    }
}
