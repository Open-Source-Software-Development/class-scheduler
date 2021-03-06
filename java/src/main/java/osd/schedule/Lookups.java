package osd.schedule;

import osd.database.input.Professor;
import osd.database.input.Section;

import java.util.stream.Stream;

/**
 * Additional information available to base constraints and base preferences.
 */
public interface Lookups {

    /**
     * Gets all the hunks that have been scheduled thus far. If no hunks have
     * been scheduled, returns an empty stream.
     * @return all the hunks that have been scheduled thus far
     */
    Stream<Hunk> lookupAllHunks();

    /**
     * Get all the hunks for which a professor is scheduled. If the professor
     * has not yet been scheduled anywhere, return an empty stream.
     * @param professor the professor to look up hunks for
     * @return a (possibly empty, non-null) hunk stream for that professor
     */
    Stream<Hunk> lookup(Professor professor);

    /**
     * Find the hunk for a section. If the section has not been scheduled yet,
     * return {@code null}.
     * @param section the section to look up a hunk for
     * @return the (possibly null) hunk for that section
     */
    Hunk lookup(Section section);

}
