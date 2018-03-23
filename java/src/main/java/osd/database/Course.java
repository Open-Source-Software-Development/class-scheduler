package osd.database;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Represents a specific course. The most important element of a course is its
 * sections, so that's what this focuses on.
 */
public interface Course extends Named {

    /**
     * Return all the sections for this course. This should consider both
     * "pregenerated" sections specified in the database, and "additional"
     * sections specified in the course's "sections" column.
     * @return an iterable representing this course's sections
     */
    Iterable<Section> getSections();

    /**
     * Indicate how to generate block pairs for this course.
     * @return how to generate block pairs for this course
     * @see BlockingStrategy
     */
    default Function<Block, Stream<Block>> getBlockingStrategy() {
        return BlockingStrategy.PAIR;
    }

}
