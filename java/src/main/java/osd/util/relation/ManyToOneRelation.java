package osd.util.relation;

import java.util.HashSet;
import java.util.Set;

public class ManyToOneRelation<K, V> extends Relation<K, Set<K>, V, V> {

    public ManyToOneRelation() {}

    public ManyToOneRelation(final Relation<K, Set<K>, V, V> copyOf) {
        super(copyOf);
    }

    protected ManyToOneRelation(final Relation<V, V, K, Set<K>> reverseOf, Void disambiguate) {
        super(reverseOf, disambiguate);
    }

    @Override
    public void add(final K key, final V value) {
        reversed().add(value, key);
    }

    @Override
    public void remove(final K key, final V value) {
        reversed().remove(value, key);
    }

    @Override
    public void remove(final K key) {
        final V value = data.remove(key);
        if (value == null) {
            return;
        }
        final Set<K> keys = reversed.get(value);
        keys.remove(key);
        if (keys.isEmpty()) {
            reversed.remove(value);
        }
    }

    @Override
    public Relation<V, V, K, Set<K>> reversed() {
        return new OneToManyRelation<>(this, null);
    }

}
