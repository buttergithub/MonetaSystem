package org.moneta.monetasys.backend;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Repository<T> {
    private final Map<String, T> records = new HashMap<>();

    public void add(String key, T value) {
        records.put(key, value);
    }

    public T findByKey(String key) {
        return records.get(key);
    }

    public boolean contains(String key) {
        return records.containsKey(key);
    }

    public void remove(String key) {
        records.remove(key);
    }

    public Collection<T> findAll() {
        return records.values();
    }

    public void clear() {
        records.clear();
    }

    public int size() {
        return records.size();
    }
}