package osd.util;

/**
 * A pair of two elements.
 * <p>Like {@linkplain java.util.Collection the Collections framework},
 * {@code Pair} redefines {@link #equals(Object)} to care only about the
 * elements of the pair, not the specific implementation. Consult the
 * documentation for {@link #equals(Object)} {@link #hashCode()} for
 * further information.</p>
 * @param <L> the type of the left-hand element
 * @param <R> the type of the right-hand element
 * @see ImmutablePair#of
 */
public interface Pair<L, R> {

    /**
     * Get whatever's on the left-hand side of this pair.
     * @return whatever's on the left-hand side of this pair
     */
    L left();

    /**
     * Get whatever's on the right-hand side of this pair.
     * @return whatever's on the right-hand side of this pair
     */
    R right();

    /**
     * Get an immutable copy of this pair. If this pair is already immutable,
     * return the pair itself. Otherwise, construct a new immutable pair
     * with the same elements. If the pair was mutable, changes will not be
     * reflected in the immutable copy.
     * @return an immutable view of this pair
     */
    ImmutablePair<L, R> immutable();

    default boolean isEmpty() {
        return left() == null && right() == null;
    }

    /**
     * Check if this pair is equal to another object. If the other object is
     * not a pair, return {@code false}. Otherwise, return whether this pair
     * has the same elements in the same order. This should <em>not</em> check
     * the specific class.
     * @param o another object
     * @return whether that object is a pair with the same elements
     */
    @Override
    boolean equals(final Object o);

    /**
     * Return {@code Objects.hash} on the left and right elements, in that order.
     * This specific implementation ensures {@code hashCode}-based containers
     * will handle equal pairs correctly, regardless of the specific
     * implementation.
     * @return {@code Objects.hash} of the left and right elements
     */
    @Override
    int hashCode();

}
