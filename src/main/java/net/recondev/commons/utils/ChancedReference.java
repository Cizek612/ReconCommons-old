package net.recondev.commons.utils;

import java.util.Random;
@SuppressWarnings("unused")
public final class ChancedReference<T> {
    private static final Random CHANCED_RANDOM = new Random();
    private final double chance;
    private final T reference;

    public ChancedReference(final T reference) {
        this(100.0, reference);
    }

    public ChancedReference(final double chance, final T reference) {
        this.chance = chance;
        this.reference = reference;
    }

    public boolean chance() {
        return CHANCED_RANDOM.nextDouble() * 100.0 < this.chance;
    }

    public boolean chance(final double boost) {
        return CHANCED_RANDOM.nextDouble() * 100.0 < this.chance + boost;
    }

    public T getReference() {
        return this.reference;
    }
}