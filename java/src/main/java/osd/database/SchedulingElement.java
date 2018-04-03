package osd.database;

import java.util.Objects;

class SchedulingElement extends AbstractSchedulingElement {

    private final String name;

    SchedulingElement(final String name) {
        this.name = Objects.requireNonNull(name);
    }

    @Override
    String getName() {
        return name;
    }
}
