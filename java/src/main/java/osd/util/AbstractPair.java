package osd.util;

import java.util.Objects;

abstract class AbstractPair<L, R> implements Pair<L, R> {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;
        Pair<?, ?> that = (Pair<?, ?>) o;
        return Objects.equals(left(), that.left()) &&
                Objects.equals(right(), that.right());
    }

    @Override
    public int hashCode() {
        return Objects.hash(left(), right());
    }

    @Override
    public String toString() {
        return "(" + left() + ", " + right() + ")";
    }

}
