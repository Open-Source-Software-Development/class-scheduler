package osd.schedule;

import osd.input.Section;
import osd.output.Hunk;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Stream;

class WIPSchedule {

    private final List<Hunk> result;
    private final Priority priority;
    private final Preferences preferences;

    @Inject
    WIPSchedule(final Priority priority, final Preferences preferences) {
        this.result = new ArrayList<>();
        this.priority = priority;
        this.preferences = preferences;
    }

    private WIPSchedule(final WIPSchedule copyOf, final Hunk withHunk) {
        this.result = new ArrayList<>(copyOf.result);
        this.priority = copyOf.priority.copy();
        this.preferences = copyOf.preferences;
        this.result.add(withHunk);
        this.priority.onHunkAdded(withHunk);
    }

    List<Hunk> getResult() {
        if (isComplete()) {
            return Collections.unmodifiableList(result);
        }
        return streamNextGeneration()
                .map(WIPSchedule::getResult)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private boolean isComplete() {
        return priority.getHighPrioritySection() == null;
    }

    private Stream<WIPSchedule> streamNextGeneration() {
        final Section section = priority.getHighPrioritySection();
        return streamCandidates(section);
    }

    private Stream<WIPSchedule> streamCandidates(final Section section) {
        return priority.getCandidateHunks(section)
                .sorted(preferences)
                .map(h -> new WIPSchedule(this, h));
    }

}
