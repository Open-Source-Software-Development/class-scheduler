package osd.input.placeholder;

import osd.input.Course;
import osd.input.Section;

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
