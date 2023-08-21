package net.recondev.commons.storage.json;

import lombok.SneakyThrows;
import net.recondev.commons.ReconCommons;
import net.recondev.commons.patterns.Service;
import net.recondev.commons.storage.SingleKeyedStorage;
import net.recondev.commons.storage.patterns.service.StorageService;


import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.Collection;

@SuppressWarnings("all")
public abstract class SingleKeyedJsonStorage<T> implements SingleKeyedStorage<T> {

    private final Service<T> service;
    private final File file;

    @SneakyThrows
    public SingleKeyedJsonStorage(final File file, final Class<T> type, final Service<T> service) {
        this.file = file;
        this.service = service;

        for(final T value : (T[]) ReconCommons.getGson().fromJson(new FileReader(this.file), Array.newInstance(type, 0).getClass())) {
            this.save(value);
        }
    }

    public SingleKeyedJsonStorage(final File file, final Class<T> type) {
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
}
