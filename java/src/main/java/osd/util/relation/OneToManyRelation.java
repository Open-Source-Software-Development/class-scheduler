package osd.util.relation;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OneToManyRelation<K, V> extends Relation<K, K, V, Set<V>> {

    /**
     * Constructs a new relation with no data.
     */
    public OneToManyRelation() {}

    /**
     * Copy constructor. The original relation and the copy are completely
     * separate; changes to one will not be reflected in the other.
     * @param copyOf the relation to copy
     */
    public OneToManyRelation(final Relation<K, K, V, Set<V>> copyOf) {
        super(copyOf);
        forward.replaceAll((k, v) -> new HashSet<>(v));
    }

    /**
     * View constructor. The relation's maps are supplied directly, and it
     * will read and write to them as appropriate. Be careful with this.
     * @param forward the forward map
     * @param reverse the reverse map
     */
    protected OneToManyRelation(final Map<K, Set<V>> forward, final Map<V, K> reverse) {
        super(forward, reverse);
    }

    @Override
    public void add(final K key, final V value) {
        if (reverse.containsKey(value)) {
            final K oldKey = reverse.get(value);
            final Set<V> oldValues = forward.get(oldKey);
            if (oldValues != null) {
                oldValues.remove(value);
                if (oldValues.isEmpty()) {
                    forward.remove(oldKey);
                }
            }
        }
        assert get(key) == null || !get(key).isEmpty();
        final Set<V> values = forward.computeIfAbsent(key, z -> new HashSet<>());
        values.add(value);
        reverse.put(value, key);
    }

    @Override
    public void remove(final K key, final V value) {
        final Set<V> values = forward.get(key);
        if (values == null || !values.remove(value)) {
            return;
        }
        reverse.remove(value);
        if (values.isEmpty()) {
            forward.remove(key);
        }
        assert get(key) == null || !get(key).isEmpty();
    }

    @Override
    public void remove(final K key) {
        final Set<V> values = forward.remove(key);
        for (final V value: values) {
            reverse.remove(value, key);
        }
        assert get(key) == null || !get(key).isEmpty();
    }

    @Override
    public ManyToOneRelation<V, K> reversed() {
        return new ManyToOneRelation<>(this.reverse, this.forward);
    }

}
