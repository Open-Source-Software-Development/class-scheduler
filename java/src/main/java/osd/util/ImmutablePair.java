package osd.util;

/**
 * A {@link Pair} that is explicitly immutable.
 * @param <L> the left-hand type
 * @param <R> the right-hand type
 */
public interface ImmutablePair<L, R> extends Pair<L, R> {

    default ImmutablePair<L, R> immutable() {
        return this;
    }

    static <L, R> ImmutablePair<L, R> of(final L left, final R right) {
        return new ImmutablePairImpl<>(left, right);
    }

}
