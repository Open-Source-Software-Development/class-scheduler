package osd.util.relation;

import java.util.HashSet;
import java.util.Set;

public class OneToManyRelation<K, V> extends Relation<K, K, V, Set<V>> {

    public OneToManyRelation() {}

    public OneToManyRelation(final Relation<K, K, V, Set<V>> copyOf) {
        super(copyOf);
    }

    OneToManyRelation(final Relation<V, Set<V>, K, K> reverseOf, Void disambiguate) {
        super(reverseOf, disambiguate);
    }

    @Override
    public void add(final K key, final V value) {
        if (reversed.containsKey(value)) {
            final K oldKey = reversed.get(value);
            final Set<V> oldValues = data.get(oldKey);
            if (oldValues != null) {
                oldValues.remove(value);
                if (oldValues.isEmpty()) {
                    data.remove(oldKey);
                }
            }
        }
        final Set<V> values = data.computeIfAbsent(key, z -> new HashSet<>());
        values.add(value);
        reversed.put(value, key);
    }

    @Override
    public void remove(final K key, final V value) {
        final Set<V> values = data.get(key);
        if (values == null || !values.remove(value)) {
            return;
        }
        reversed.remove(value, key);
        if (values.isEmpty()) {
            data.remove(key);
        }
    }

    @Override
    public void remove(final K key) {
        final Set<V> values = data.remove(key);
        for (final V value: values) {
            reversed.remove(value, key);
        }
    }

    @Override
    public Relation<V, Set<V>, K, K> reversed() {
        return new ManyToOneRelation<>(this, null);
    }

}
