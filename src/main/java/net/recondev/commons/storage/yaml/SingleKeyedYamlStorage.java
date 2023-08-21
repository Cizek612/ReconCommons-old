package net.recondev.commons.storage.yaml;

import lombok.SneakyThrows;
import net.recondev.commons.patterns.Service;
import net.recondev.commons.storage.SingleKeyedStorage;
import net.recondev.commons.storage.patterns.service.StorageService;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collection;

@SuppressWarnings({"unchecked", "unused", "rawtypes"})
public abstract class SingleKeyedYamlStorage<T> implements SingleKeyedStorage<T> {

    private final Service<T> service;
    private final File file;
    private final Class<T> type;
    private final Yaml yaml;

    @SneakyThrows
    public SingleKeyedYamlStorage(final File file, final Class<T> type, final Service<T> service) {
        this.file = file;
        this.service = service;
        this.type = type;
        this.yaml = new Yaml();

        try (final FileReader reader = new FileReader(this.file)) {
            final Iterable<Object> values = yaml.loadAll(reader);
            for (final Object value : values) {
                final T castValue = (T) value;
                this.save(castValue);
            }
        }
    }

    public SingleKeyedYamlStorage(final File file, final Class<T> type) {
        this(file, type, new StorageService());
    }

    @Override
    public Service<T> service() {
        return this.service;
    }

    @Override
    public boolean contains(final T value) {
        return service.contains(value);
    }

    @Override
    public void remove(final T value) {
        this.service.unregister(value);
    }

    @Override
    public void save(final T value) {
        this.service.register(value);
    }

    @Override
    public void saveAll(final Collection<T> values) {
        values.forEach(this::save);
    }

    @SneakyThrows
    @Override
    public void write() {
        try (final FileWriter writer = new FileWriter(this.file)) {
            yaml.dumpAll(this.service.values().iterator(), writer);
        }
    }
}