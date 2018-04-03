package osd.database;

import java.util.Objects;

class SchedulingElement extends AbstractSchedulingElement {

    private final int id;
    private final String name;

    SchedulingElement(final int id, final String name) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
    }

    @Override
    int getId() {
        return id;
    }

    @Override
    String getName() {
        return name;
    }
}
