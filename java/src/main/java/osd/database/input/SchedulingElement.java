package osd.database.input;

import java.util.Objects;

public class SchedulingElement {

    private final int id;
    private final String name;

    SchedulingElement(final int id, final String name) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
    }

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
