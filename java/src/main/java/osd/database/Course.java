package osd.database;

import java.util.Collections;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Represents a specific course. The most important element of a course is its
 * sections, so that's what this focuses on.
 */
public class Course extends SchedulingElement {

    private final int baseSectionCount;

    Course(final int id, final String name, final int baseSectionCount) {
        super(id, name);
        this.baseSectionCount = baseSectionCount;
    }

    /**
     * Return all the sections for this course. This should consider both
     * "pregenerated" sections specified in the database, and "additional"
     * sections specified in the course's "sections" column.
     * @return an iterable representing this course's sections
     */
    Iterable<Section> getSections() {
        return () -> IntStream.rangeClosed(1, baseSectionCount)
                .mapToObj(i -> Section.of(this, String.valueOf(i)))
                .iterator();
    }

    /**
     * Indicate how to generate block pairs for this course.
     * @return how to generate block pairs for this course
     * @see BlockingStrategy
     */
    public Function<Block, Stream<Block>> getBlockingStrategy() {
        // TODO: proper implementation
        return BlockingStrategy.PAIR;
    }

}
