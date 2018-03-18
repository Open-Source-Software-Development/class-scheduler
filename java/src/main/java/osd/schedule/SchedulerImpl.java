package osd.schedule;

import osd.input.Section;
import osd.output.Hunk;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Stream;

/**
 * A recursive, streaming {@link Scheduler} implementation. Each "generation"
 * of the backtracking solution generates new {@code SchedulerImpl} instances
 * with more data, until an instance with a complete schedule is generated.
 */
class SchedulerImpl implements Scheduler {

    private final List<Hunk> result;
    private final Priority priority;
    private final Preferences preferences;

    @Inject
    SchedulerImpl(final Priority priority, final Preferences preferences) {
        this.result = new ArrayList<>();
        this.priority = priority;
        this.preferences = preferences;
    }

    private SchedulerImpl(final SchedulerImpl copyOf, final Hunk withHunk) {
        this.result = new ArrayList<>(copyOf.result);
        this.priority = copyOf.priority.copy();
        this.preferences = copyOf.preferences;
        this.result.add(withHunk);
        this.priority.onHunkAdded(withHunk);
    }

    @Override
    public List<Hunk> getResult() {
        // Stop condition: If this is a complete schedule, return our list.
        if (isComplete()) {
            return Collections.unmodifiableList(result);
        }

        // Recurse: Visit every candidate in the next generation (depth-first),
        // and ask it for its result. If any gives us a successful result, our
        // result is its result. Otherwise, our result is also a failure
        // result.
        return streamNextGeneration()
                .map(Scheduler::getResult)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private boolean isComplete() {
        return priority.getHighPrioritySection() == null;
    }

    private Stream<Scheduler> streamNextGeneration() {
        final Section section = priority.getHighPrioritySection();
        return streamCandidates(section);
    }

    private Stream<Scheduler> streamCandidates(final Section section) {
        return priority.getCandidateHunks(section)
                .sorted(preferences)
                .map(h -> new SchedulerImpl(this, h));
    }

}
