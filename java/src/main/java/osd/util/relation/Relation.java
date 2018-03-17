package osd.util.relation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class Relation<K, L, V, W> {

    protected final Map<K, W> data;
    protected final Map<V, L> reversed;

    public Relation() {
        this.data = new HashMap<>();
        this.reversed = new HashMap<>();
    }

    public Relation(final Relation<K, L, V, W> copyOf) {
        this.data = new HashMap<>(copyOf.data);
        this.reversed = new HashMap<>(copyOf.reversed);
    }

    protected Relation(final Relation<V, W, K, L> reverseOf, Void disambiguate) {
        this.data = reverseOf.reversed;
        this.reversed = reverseOf.data;
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public abstract void add(final K key, final V value);

    public abstract void remove(final K key, final V value);

    public abstract void remove(final K key);

    public W get(final K key) {
        return data.get(key);
    }

    public W getOrDefault(final K key, final Supplier<W> defaultSupplier) {
        final W w = get(key);
        return w == null ? defaultSupplier.get() : w;
    }

    public abstract Relation<V, W, K, L> reversed();

    @Override
    public String toString() {
        return data.toString();
    }

}
