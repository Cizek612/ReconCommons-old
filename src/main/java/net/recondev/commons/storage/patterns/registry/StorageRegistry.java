package net.recondev.commons.storage.patterns.registry;

import lombok.Getter;
import net.recondev.commons.patterns.Registry;

import java.util.HashMap;
import java.util.Map;

public class StorageRegistry<K, V> implements Registry<K, V> {

    @Getter public final Map<K, V> registry = new HashMap<>();
}
