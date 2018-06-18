package osd.database.input;

import osd.database.Identified;

import java.util.Objects;

class SchedulingElement implements Identified {

    private final int id;
    private final String name;

    SchedulingElement(final int id, final String name) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
    }

    @Override
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return getId();
    }

    @Override
    public boolean equals(final Object o) {
        return o != null && getClass() == o.getClass() && id == ((SchedulingElement)o).id;
    }

    @Override
    public String toString() {
        return getName();
    }

}
