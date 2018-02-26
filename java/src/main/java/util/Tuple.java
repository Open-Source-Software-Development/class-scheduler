package util;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public class Tuple<L, R> {

    private final L left;
    private final R right;

    public Tuple(final L left, final R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() {
        return left;

    }

    public R getRight() {
        return right;
    }

    public <A> Tuple<A, Tuple<L, R>> cons(final A a) {
        return new Tuple<>(a, this);
    }

    public static <L, R> Function<R, Tuple<L, R>> cons2(final L l) {
        return r -> new Tuple<>(l, r);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Tuple<?, ?> other = (Tuple<?, ?>)o;
        return Objects.equals(left, other.left) && Objects.equals(right, other.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    @Override
    public String toString() {
        return "(" + left + ", " + right + ")";
    }

}
