package osd.database.placeholder;

import osd.database.Course;
import osd.database.Section;

import java.util.Collections;

class CoursePlaceholder extends NamedPlaceholder implements Course {

    CoursePlaceholder(final String[] row) {
        super(row);
    }

    @Override
    public Iterable<Section> getSections() {
        return Collections.singleton(Section.of(this, "1"));
    }
}
