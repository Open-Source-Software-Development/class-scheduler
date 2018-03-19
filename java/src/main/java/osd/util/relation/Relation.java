package osd.util.relation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A reversible relation. Relations come in four flavors:
 * <ul>
 *     <li>One-to-one,</li>
 *     <li>One-to-many,</li>
 *     <li>Many-to-one,</li>
 *     <li>Many-to-many.</li>
 * </ul>
 * The relation enforces these constraints, as appropriate. For example, a
 * one-to-many relation will allow the same key to be associated with many
 * values. However, if a second key would be associated with a value,
 * the former key is first disassociated.
 * <p>One-to-many and many-to-many relations return their values as sets of
 * whatever the value type is. One-to-one and many-to-one relations don't need
 * to, and return instances of the value type directly. If a key isn't
 * associated with anything, {@code null} is returned to indicate this (if the
 * relationship is one-to-many or many-to-many, a lookup will <em>never</em>
 * return an empty set.</p>
 * <p>All four kinds of relations can be reversed, yielding a new relation
 * whose values are the keys of the original, and whose keys are its values.
 * One-to-many and many-to-one relations reverse to each other.</p>
 * @param <K> the type of key
 * @param <L> either K (one-to-one or one-to-many) or {@code Set<K>} (otherwise)
 * @param <V> the type of value
 * @param <W> either V (one-to-one or many-to-one) or {@code Set<V>} (otherwise)
 */
public abstract class Relation<K, L, V, W> {

    /**
     * This relation's data, in "forward" order. This map supports mutation
     * operations, but be <strong>very careful</strong> using them! It is
     * recommended to use the methods of {@code Relation} to avoid misaligned
     * data.
     */
    protected final Map<K, W> forward;

    /**
     * This relation's data, in "reverse" order. This map supports mutation
     * operations, but be <strong>very careful</strong> using them! It is
     * recommended to use the methods of {@code Relation} to avoid misaligned
     * data.
     */
    protected final Map<V, L> reverse;

    /**
     * Constructs a new relation with no data.
     */
    Relation() {
        this.forward = new HashMap<>();
        this.reverse = new HashMap<>();
    }

    /**
     * Copy constructor. The original relation and the copy are completely
     * separate; changes to one will not be reflected in the other.
     * @param copyOf the relation to copy
     */
    Relation(final Relation<K, L, V, W> copyOf) {
        this.forward = new HashMap<>(copyOf.forward);
        this.reverse = new HashMap<>(copyOf.reverse);
    }

    /**
     * View constructor. The relation's maps are supplied directly, and it
     * will read and write to them as appropriate. Be careful with this.
     * @param forward the forward map
     * @param reverse the reverse map
     */
    Relation(final Map<K, W> forward, final Map<V, L> reverse) {
        this.forward = forward;
        this.reverse = reverse;
    }

    /**
     * Checks if this relation is empty. That is, if this return {@code true},
     * it is impossible for a call to {@link #get(Object)} to return non-{@code null}.
     * @return whether this relation is empty
     */
    public boolean isEmpty() {
        return forward.isEmpty();
    }

    /**
     * Associates a key and a value. If this relation is one-to-one or
     * one-to-many, any previous associations to the given value are removed.
     * Likewise, if this relation is one-to-one or many-to-one, any previous
     * associations from that key are removed.
     * @param key the key to associate with a new value
     * @param value the value to associate with a new key
     */
    public abstract void add(final K key, final V value);

    /**
     * Disassociates a key and a value. If they were previously associated,
     * this has no effect. Otherwise, they are no longer associated.
     * @param key the key to disassociate from a value
     * @param value the value to disassociate from a key
     */
    public abstract void remove(final K key, final V value);

    /**
     * Disassociates a key from any values it's associated with.
     * @param key the key to disassociate from all values
     * @see Relation#reversed()
     */
    public abstract void remove(final K key);

    /**
     * Gets the value(s) a key is associated with. If this relation is
     * one-to-many or many-to-many, this returns a {@link java.util.Set} of the
     * appropriate value type. Otherwise, it returns a single value. If the key
     * is absent, this always returns {@code null}.
     * <p>If a set is returned, it will support mutation operations, but
     * modifications to that set will <em>not</em> write through to the
     * relation, nor will changes to the relation be reflected in the set.
     * This method will never return an empty set.</p>
     * <p>To </p>
     * @param key the key to get values for
     * @return the value or set of values
     * @see Relation#reversed()
     */
    public W get(final K key) {
        return forward.get(key);
    }

    /**
     * Gets the value(s) a key is associated with, or a default. If the key is
     * present, it is returned as with {@link #get(Object)}. Otherwise, the
     * provided {@link Supplier} is invoked to get a default, which is
     * returned. This is most useful for *-to-many relations, where
     * {@code Collections::emptySet} may be given if an empty set is desired
     * instead of {@code null}.
     * @param key the key to get values for
     * @param defaultSupplier a supplier for default values
     * @return the value or set of values, or the default
     * @see java.util.Collections#emptySet()
     */
    public W getOrDefault(final K key, final Supplier<W> defaultSupplier) {
        final W w = get(key);
        return w == null ? defaultSupplier.get() : w;
    }

    /**
     * Gets a backwards view of this relation. If this relation is one-to-many,
     * the view is many-to-one, and vice-verse. If it is one-to-one or
     * many-to-many, the view has the same cardinality. Changes to the view
     * write through to the original relation, and vice-verse. Since the keys
     * and values are interchanged in the view, this is particularly useful for
     * getting the key(s) associated with a value, or with the single-argument
     * form of {@link #remove(Object)}.
     * @return a reversed view of this relation
     */
    public abstract Relation<V, W, K, L> reversed();

    @Override
    public String toString() {
        return forward.toString();
    }

}
