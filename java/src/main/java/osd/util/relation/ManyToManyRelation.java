package osd.util.relation;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ManyToManyRelation<K, V> extends Relation<K, Set<K>, V, Set<V>> {

    public ManyToManyRelation() {}

    public ManyToManyRelation(final Relation<K, Set<K>, V, Set<V>> copyOf) {
        super(copyOf);
    }

    protected ManyToManyRelation(final Relation<V, Set<V>, K, Set<K>> reverseOf, final Void disambiguate) {
        super(reverseOf, disambiguate);
    }

    @Override
    public void add(final K key, final V value) {
        data.computeIfAbsent(key, z -> new HashSet<>()).add(value);
        reversed.computeIfAbsent(value, z -> new HashSet<>()).add(key);
    }

    @Override
    public void remove(final K key, final V value) {
        remove0(data, key, value);
        remove0(reversed, value, key);
    }

    @Override
    public void remove(final K key) {
        if (!data.containsKey(key)) {
            return;
        }
        final Set<V> values = data.remove(key);
        for (final V value: values) {
            remove0(reversed, value, key);
        }
    }

    private <A, B> void remove0(final Map<A, Set<B>> map, final A a, final B b) {
        if (!map.containsKey(a)) {
            return;
        }
        final Set<B> set = map.get(a);
        set.remove(b);
        if (set.isEmpty()) {
            map.remove(a);
        }
    }

    @Override
    public ManyToManyRelation<V, K> reversed() {
        return new ManyToManyRelation<>(this, null);
    }

}
