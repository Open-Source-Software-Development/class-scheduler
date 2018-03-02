package osd.schedule;

import osd.input.Section;
import osd.output.Hunk;

import java.util.function.Function;
import java.util.stream.Stream;

interface CandidateHunkSource extends Function<Section, Stream<Hunk>> {

    default Iterable<Hunk> iterable(final Section section) {
        return () -> apply(section).iterator();
    }

}
