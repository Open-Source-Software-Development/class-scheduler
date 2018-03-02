package osd.schedule;

import osd.input.Section;
import osd.output.Hunk;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Generates candidate {@link Hunk}s for a course section. This is implemented
 * as a {@link Stream stream} of hunks for some {@linkplain Hunk#getSection() section}.
 * This should try to return "high quality" hunks first, when possible, but
 * should <em>not</em> try to filter out hunks that violate constraints. (It
 * may, however, elect to return them late in the stream.)
 */
interface CandidateHunkSource extends Function<Section, Stream<Hunk>> {

    /**
     * Generates an {@link Iterable} representing the hunk stream for some section.
     * This is provided mainly as a convenience for for-each loops; the
     * iterable is, however, reusable as specified in the {@code Iterable}
     * contract.
     * @param section the section
     * @return an iterable representing the hunk stream for that section
     */
    default Iterable<Hunk> iterable(final Section section) {
        return () -> apply(section).iterator();
    }

}
