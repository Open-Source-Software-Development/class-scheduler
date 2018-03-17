package osd.util.relation;

import java.util.Map;
import java.util.Set;

public class ManyToOneRelation<K, V> extends Relation<K, Set<K>, V, V> {

    /**
     * Constructs a new relation with no data.
     */
    public ManyToOneRelation() {}

    /**
     * Copy constructor. The original relation and the copy are completely
     * separate; changes to one will not be reflected in the other.
     * @param copyOf the relation to copy
     */
    public ManyToOneRelation(final Relation<K, Set<K>, V, V> copyOf) {
        super(copyOf);
    }

    /**
     * View constructor. The relation's maps are supplied directly, and it
     * will read and write to them as appropriate. Be careful with this.
     * @param forward the forward map
     * @param reverse the reverse map
     */
    protected ManyToOneRelation(final Map<K, V> forward, final Map<V, Set<K>> reverse) {
        super(forward, reverse);
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
        final V value = forward.remove(key);
        if (value == null) {
            return;
        }
        final Set<K> keys = reverse.get(value);
        keys.remove(key);
        if (keys.isEmpty()) {
            reverse.remove(value);
        }
    }

    @Override
    public Relation<V, V, K, Set<K>> reversed() {
        return new OneToManyRelation<>(this.reverse, this.forward);
    }

}
