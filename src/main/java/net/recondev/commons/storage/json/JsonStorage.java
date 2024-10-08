package net.recondev.commons.storage.json;

import lombok.SneakyThrows;
import net.recondev.commons.ReconCommons;
import net.recondev.commons.patterns.Registry;
import net.recondev.commons.storage.Storage;
import net.recondev.commons.storage.id.utils.IdFinder;
import net.recondev.commons.storage.patterns.registry.StorageRegistry;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.Collection;

@SuppressWarnings({"unused", "rawtypes", "unchecked"})
public abstract class JsonStorage<K, V> implements Storage<K, V> {

    public final File file;
    private final Class<V> type;
    private final Registry<K, V> registry;

    @SneakyThrows
    public JsonStorage(final File file, final Class<V> type, final Registry<K, V> registry) {
        this.file = file;
        this.type = type;
        this.registry = registry;

        for (final V value : (V[]) ReconCommons.getGson().fromJson(new FileReader(this.file), Array.newInstance(type, 0).getClass())) {
            this.registry.register((K) IdFinder.getId(type, value), value);
        }

    }

    public JsonStorage(final File file, final Class<V> type) {
        this(file, type, new StorageRegistry());
    }

    @Override
    public boolean contains(final K key) {
        return this.registry.containsKey(key);
    }

    @Override
    public Registry<K, V> registry() {
        return this.registry;
    }

    @Override
    public Collection<K> keys() {
        return this.registry.keySet();
    }

    @Override
    public Collection<V> values() {
        return this.registry.values();
    }

    @Override
    public V get(final K key) {
        return this.registry.getRegistry().getOrDefault(key, null);
    }

    @Override
    public void remove(final K key) {
        this.registry.unregister(key);
    }

    @Override
    public void save(final V value) {
        this.registry.register( (K) IdFinder.getId(this.type, value), value);
    }

    @Override
    public void saveAll(final Collection<V> collection) {
        collection.forEach(value -> this.registry.register( (K) IdFinder.getId(this.type, value), value));
    }

    @SneakyThrows @Override
    public void write() {
        final Writer writer = new FileWriter(this.file);
        ReconCommons.getGson().toJson(this.registry.values(), writer);
        writer.close();
    }
}
