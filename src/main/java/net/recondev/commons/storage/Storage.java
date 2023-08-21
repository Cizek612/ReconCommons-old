package net.recondev.commons.storage;

import net.recondev.commons.patterns.Registry;

import java.util.Collection;


public interface Storage<K, V> {
    Registry<K, V> registry();

    V get(final K key);

    void remove(final K key);

    void write();

    void save(final V value);

    void saveAll(final Collection<V> collection);

    boolean contains(final K key);

    Collection<V> values();

    Collection<K> keys();
}