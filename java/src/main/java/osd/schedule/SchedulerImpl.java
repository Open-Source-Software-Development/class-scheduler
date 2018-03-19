package osd.schedule;

import osd.input.Section;
import osd.output.Callbacks;
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
    public boolean run(final Callbacks callbacks) {
        try {
            run0(callbacks);
        } catch(final ScheduleDoneSignal s) {
            // If we catch this exception, we met the stop condition.
            return true;
        }
        // If we didn't catch any exception, we ran out of candidates first.
        return false;
    }

    private void run0(final Callbacks callbacks) {
        if (callbacks.stopCondition()) {
            System.err.flush();
            throw new ScheduleDoneSignal();
        }
        if (isComplete()) {
            callbacks.onCompleteResult(results);
        }
        final Scheduler lastChild = streamNextGeneration()
                .peek(s -> System.err.println("Descending"))
                .peek(s -> s.run0(callbacks))
                // This is a somewhat hacky reduction to ensure we
                // a) consider every child (unless we exit early), and
                // b) can tell whether or not we descended at all.
                // ASK NAOMI BEFORE CHANGING THIS, because most of the
                // other obvious solutions will violate the first objective,
                // for subtle reasons related to lazy evaluation.
                .reduce(null, (a, b) -> b);
        if (lastChild == null && !callbacks.stopCondition()) {
            System.err.println("Backtracking");
            callbacks.onBacktrack(results);
        }
    }

    private boolean isComplete() {
        return priority.getHighPrioritySection() == null;
    }

    private Stream<SchedulerImpl> streamNextGeneration() {
        final Section section = priority.getHighPrioritySection();
        return streamCandidates(section);
    }

    private Stream<SchedulerImpl> streamCandidates(final Section section) {
        return priority.getCandidateHunks(section)
                .sorted(preferences)
                .map(h -> new SchedulerImpl(this, h));
    }

}
