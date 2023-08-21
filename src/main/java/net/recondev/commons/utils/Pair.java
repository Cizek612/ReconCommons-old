package net.recondev.commons.utils;


import java.util.Map;
@SuppressWarnings("unused")
public class Pair<K, V> implements Map.Entry<K, V> {
    private K key;
    private V value;

    public Pair() {
    }

    public Pair(final K key, final V value) {
        this.key = key;
        this.value = value;
    }

    public K setKey(final K key) {
        this.key = key;
        return key;
    }

    @Override
    public V setValue(final V value) {
        this.value = value;
        return value;
    }

    @Override
    public K getKey() {
        return this.key;
    }

    @Override
    public V getValue() {
        return this.value;
    }

    public void nullify() {
        this.key = null;
        this.value = null;
    }
}
