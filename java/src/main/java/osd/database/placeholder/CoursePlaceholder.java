package osd.database.placeholder;

import osd.database.Course;
import osd.database.Section;

import java.util.stream.IntStream;

class CoursePlaceholder extends NamedPlaceholder implements Course {

    private int sectionCount;

    CoursePlaceholder(final String[] row) {
        super(row);
    }

    @FromCSV(1)
    void setSectionCount(final String sectionCount) {
        this.sectionCount = Integer.valueOf(sectionCount);
    }

    @Override
    public Iterable<Section> getSections() {
        return () -> IntStream.rangeClosed(1, sectionCount)
                .mapToObj(i -> i + "")
                .map(i -> Section.of(this, i))
                .iterator();
    }
}
