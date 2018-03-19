package osd.schedule;

import osd.input.Section;
import osd.output.Hunk;
import osd.output.Results;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Stream;

/**
 * A recursive, streaming {@link Scheduler} implementation. Each "generation"
 * of the backtracking solution generates new {@code SchedulerImpl} instances
 * with more data, until an instance with a complete schedule is generated.
 */
class SchedulerImpl implements Scheduler {

    private final SchedulerResults results;
    private final Priority priority;
    private final Preferences preferences;

    @Inject
    SchedulerImpl(final Priority priority, final Preferences preferences) {
        this.results = SchedulerResults.empty();
        this.priority = priority;
        this.preferences = preferences;
    }

    private SchedulerImpl(final SchedulerImpl copyOf, final Hunk withHunk) {
        this.results = copyOf.results.extend(withHunk);
        this.priority = copyOf.priority.rebind(this.results);
        this.preferences = copyOf.preferences;
        this.priority.onHunkAdded(withHunk);
    }

    @Override
    public Results getResults() {
        // Stop condition: If this is a complete schedule, return our list.
        if (isComplete()) {
            return results;
        }

        // Recurse: Visit every candidate in the next generation (depth-first),
        // and ask it for its result. If any gives us a successful result, our
        // result is its result. Otherwise, our result is also a failure
        // result.
        return streamNextGeneration()
                .map(Scheduler::getResults)
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
