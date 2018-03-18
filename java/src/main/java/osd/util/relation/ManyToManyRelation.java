package osd.util.relation;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ManyToManyRelation<K, V> extends Relation<K, Set<K>, V, Set<V>> {

    /**
     * Constructs a new relation with no data.
     */
    public ManyToManyRelation() {}

    /**
     * Copy constructor. The original relation and the copy are completely
     * separate; changes to one will not be reflected in the other.
     * @param copyOf the relation to copy
     */
    public ManyToManyRelation(final Relation<K, Set<K>, V, Set<V>> copyOf) {
        super(copyOf);
        forward.replaceAll((k, v) -> new HashSet<>(v));
        reverse.replaceAll((k, v) -> new HashSet<>(v));
    }

    /**
     * View constructor. The relation's maps are supplied directly, and it
     * will read and write to them as appropriate. Be careful with this.
     * @param forward the forward map
     * @param reverse the reverse map
     */
    protected ManyToManyRelation(final Map<K, Set<V>> forward, final Map<V, Set<K>> reverse) {
        super(forward, reverse);
    }

    @Override
    public void add(final K key, final V value) {
        forward.computeIfAbsent(key, z -> new HashSet<>()).add(value);
        reverse.computeIfAbsent(value, z -> new HashSet<>()).add(key);
    }

    @Override
    public void remove(final K key, final V value) {
        remove0(forward, key, value);
        remove0(reverse, value, key);
    }

    @Override
    public void remove(final K key) {
        if (!forward.containsKey(key)) {
            return;
        }
        final Set<V> values = forward.remove(key);
        for (final V value: values) {
            remove0(reverse, value, key);
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
        return new ManyToManyRelation<>(this.reverse, this.forward);
    }

}
