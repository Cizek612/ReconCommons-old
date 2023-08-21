package net.recondev.commons.storage.yaml;

import lombok.SneakyThrows;
import net.recondev.commons.patterns.Registry;
import net.recondev.commons.storage.Storage;
import net.recondev.commons.storage.id.utils.IdFinder;
import net.recondev.commons.storage.patterns.registry.StorageRegistry;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Collection;

@SuppressWarnings({"unchecked", "unused", "rawtypes"})
public class YamlStorage<K, V> implements Storage<K, V> {

    public final File file;
    private final Class<V> type;
    private final Registry<K, V> registry;
    private final Yaml yaml;

    @SneakyThrows
    public YamlStorage(final File file, final Class<V> type, final Registry<K, V> registry) {
        this.file = file;
        this.type = type;
        this.registry = registry;
        this.yaml = new Yaml(new DumperOptions());

        try (final FileReader reader = new FileReader(this.file)) {
            final Iterable<Object> values = yaml.loadAll(reader);
            for (final Object value : values) {
                final V castValue = (V) value;
                this.registry.register((K) IdFinder.getId(type, castValue), castValue);
            }
        }

    }

    public YamlStorage(final File file, final Class<V> type) {
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
        try (final Writer writer = new FileWriter(this.file)) {
            yaml.dumpAll(this.registry.values().iterator(), writer);
        }
    }
}

