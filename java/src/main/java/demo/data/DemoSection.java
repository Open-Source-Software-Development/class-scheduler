package demo.data;

import osd.input.Course;
import osd.input.Section;

import java.util.Collection;

public class DemoSection implements Section, DemoNamed {

    private final Course course;
    private final int sectionNumber;

    public DemoSection(final Course course, final int sectionNumber) {
        this.course = course;
        this.sectionNumber = sectionNumber;
    }

    @Override
    public Iterable<Section> getSections() {
        return course.getSections();
    }

    @Override
    public String toString() {
        return course + "_" + sectionNumber;
    }

}
