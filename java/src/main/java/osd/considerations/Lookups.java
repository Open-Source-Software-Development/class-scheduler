package osd.considerations;

import osd.input.Professor;
import osd.input.Section;
import osd.output.Hunk;

import java.util.stream.Stream;

/**
 * Additional information available to base constraints and base preferences.
 */
public interface Lookups {

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
