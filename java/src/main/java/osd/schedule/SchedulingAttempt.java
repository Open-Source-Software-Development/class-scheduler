package osd.schedule;

import osd.input.Section;
import osd.output.Hunk;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * An attempt to schedule some classes. Once one is constructed, its
 * {@link #call()} method may be invoked to attempt hunk generation.
 */
public class SchedulingAttempt implements Callable<List<Hunk>> {

    private final Scheduler scheduler;

    @Inject
    SchedulingAttempt(final Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * Runs the scheduling algorithm. When the algorithm completes, returns
     * all the generated hunks as a list.
     * @return a list of generated hunks
     */
    @Override
    public List<Hunk> call() {
        return backtrackingSearch(new ArrayList<>(), scheduler);
    }

    private List<Hunk> backtrackingSearch(final List<Hunk> hunks, final Scheduler scheduler) {
        final Section section = scheduler.getNextSection();
        if (section == null) {
            return hunks;
        }
        System.out.println("Moving into " + section.getName());
        for (final Hunk hunk: scheduler.getCandidateHunks(section)) {
            final boolean wasAdded = scheduler.addHunk(hunk);
            System.out.println(hunk + " was " + (wasAdded ? "added" : "rejected"));
            if (wasAdded) {
                final List<Hunk> newHunks = new ArrayList<>(hunks);
                newHunks.add(hunk);
                final List<Hunk> result = backtrackingSearch(newHunks, scheduler.copy());
                if (result != null) {
                    return result;
                }
            }
        }
        System.out.println("Backtracking...");
        return null;
    }

}
