package osd.database;

abstract class AbstractSchedulingElement {

    abstract String getName();

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        final SchedulingElement other = (SchedulingElement)o;
        return getName().equals(other.getName());
    }

    @Override
    public String toString() {
        return getName();
    }
}
