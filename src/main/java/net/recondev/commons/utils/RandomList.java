package net.recondev.commons.utils;


import java.util.ArrayList;
import java.util.Collection;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SuppressWarnings({"unused", "unchecked"})
public final class RandomList<E> extends ArrayList<RandomList<E>.RandomCollectionObject<E>> {

    private final Random random;

    public RandomList() {
        this(new Random());
    }

    public RandomList(final Collection<? extends RandomCollectionObject<E>> c) {
        this();
        addAll(c);
    }

    public RandomList(final Random random) {
        this.random = random;
    }

    public Random getRandom() {
        return random;
    }

    public boolean add(final E e, final double chance) {
        return this.add(new RandomCollectionObject<>(e, chance));
    }

    @Override
    public boolean remove(Object o) {
        return ((RandomList<Object>) this.clone()).stream().anyMatch((t) -> t.object.equals(o) && super.remove(t));
    }

    public double totalWeight() {
        return this.stream().mapToDouble(RandomCollectionObject::getWeight).sum();
    }

    public E raffle() {
        return raffle(this);
    }

    public E raffle(final Predicate<RandomList<E>.RandomCollectionObject<E>> predicate) {
        final RandomList<E> aux = this.stream()
                .filter(predicate)
                .collect(Collectors.toCollection(RandomList::new));

        return raffle(aux);
    }

    private E raffle(final RandomList<E> list) {
       final NavigableMap<Double, RandomCollectionObject<E>> auxMap = new TreeMap<>();

        list.forEach((rco) -> {
            double auxWeight = auxMap.values().stream().mapToDouble(RandomCollectionObject::getWeight).sum();
            auxWeight += rco.getWeight();

            auxMap.put(auxWeight, rco);
        });

        final double totalWeight = list.getRandom().nextDouble() * auxMap.values().stream().mapToDouble(RandomCollectionObject::getWeight).sum();

        return auxMap.ceilingEntry(totalWeight).getValue().getObject();
    }

    public class RandomCollectionObject<T> {

        private final T object;
        private final Double weight;

        private RandomCollectionObject(final T e, final Double weight) {
            this.object = e;
            this.weight = weight;
        }

        public Double getChance() {
            return this.weight * 100 / RandomList.this.totalWeight();
        }

        public T getObject() {
            return this.object;
        }

        public Double getWeight() {
            return this.weight;
        }
    }
}
