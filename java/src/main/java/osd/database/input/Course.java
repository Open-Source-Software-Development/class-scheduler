package osd.database.input;

import osd.database.RecordAccession;
import osd.database.RecordConversion;
import osd.database.input.record.CourseRecord;

import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Represents a specific course. The most important element of a course is its
 * sections, so that's what this focuses on.
 */
public class Course extends SchedulingElement {

    private final int baseSectionCount;

    @RecordConversion
    Course(final CourseRecord record, final RecordAccession recordAccession) {
        super(record.getId(), record.getName());
        this.baseSectionCount = record.getBaseSectionCount();
    }

    /**
     * Return all the sections for this course. This should consider both
     * "pregenerated" sections specified in the database, and "additional"
     * sections specified in the course's "sections" column.
     * @return a stream of this course's sections
     */
    public Stream<Section> streamSections() {
        // TODO: consider pregen sections
        return IntStream.rangeClosed(1, baseSectionCount)
                .mapToObj(i -> Section.of(this, String.valueOf(i)));
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
