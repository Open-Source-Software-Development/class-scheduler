package osd.database;

abstract class AbstractSchedulingElement {

    abstract int getId();

    abstract String getName();

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
