package osd.database;

abstract class AbstractSchedulingElement {

    public abstract int getId();

    public abstract String getName();

    @Override
    public final int hashCode() {
        return getId();
    }

    @Override
    public final boolean equals(final Object o) {
        return o != null && getClass() == o.getClass() && hashCode() == o.hashCode();
    }

    @Override
    public String toString() {
        return getName();
    }
}
