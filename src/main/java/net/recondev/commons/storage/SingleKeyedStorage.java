package net.recondev.commons.storage;

import net.recondev.commons.patterns.Service;

import java.util.Collection;

@SuppressWarnings("unused")
public interface SingleKeyedStorage<T> {

    Service<T> service();

    void remove(final T value);

    void write();

    boolean contains(final T value);

    void save(final T value);

    void saveAll(final Collection<T> collection);
}
