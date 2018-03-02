package osd.input;

/**
 * Represents a specific course. The most important element of a course is its
 * sections, so that's what this focuses on.
 */
public interface Course extends Named {

    /**
     * Returns all the sections for this course. This should consider both
     * "pregenerated" sections specified in the database, and "additional"
     * sections specified in the course's "sections" column.
     * @return an iterable representing this course's sections
     */
    Iterable<Section> getSections();

}
