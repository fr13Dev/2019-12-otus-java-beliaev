package ru.otus.core.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    private static final String REMOVE_ACTION = "remove";
    private static final String PUT_ACTION = "put";
    private static final String GET_ACTION = "get";

    private static final Logger logger = LoggerFactory.getLogger(MyCache.class);
    private final Map<K, V> storage = new WeakHashMap<>();
    private final Set<HwListener<K, V>> listeners = Collections.newSetFromMap(new WeakHashMap<>());

    @Override
    public void put(K key, V value) {
        storage.put(key, value);
        notifyListeners(key, value, PUT_ACTION);
        logger.debug("object is cached: {}", value);
    }

    @Override
    public void remove(K key) {
        final V removedObject = storage.remove(key);
        notifyListeners(key, removedObject, REMOVE_ACTION);
        logger.debug("object is removed: {}", removedObject);
    }

    @Override
    public V get(K key) {
        final V object = storage.get(key);
        notifyListeners(key, object, GET_ACTION);
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

    private void notifyListeners(K key, V value, String action) {
        listeners.forEach(listener -> {
            try {
                listener.notify(key, value, action);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
    }
}
