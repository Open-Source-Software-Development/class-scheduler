package osd.schedule;

import osd.input.Section;
import osd.output.Hunk;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * An attempt to schedule some classes. Once one is constructed, its
 * {@link #call()} method may be invoked to attempt hunk generation.
 */
public class SchedulingAttempt implements Callable<List<Hunk>> {

    private final PriorityList<Section> sections;
    private final CandidateHunkSource candidateHunkSource;
    private final Supplier<Scheduler> schedulerFactory;

    /**
     * {@linkplain Inject Injectable} constructor. Input data is provided via
     * the {@code sections} and {@code candidateHunkSource} arguments.
     * @param schedulerFactory a supplier for blank schedulers
     * @param sections a priority list of sections
     * @param candidateHunkSource a {@linkplain CandidateHunkSource candidate hunk source}
     */
    @Inject
    SchedulingAttempt(final Supplier<Scheduler> schedulerFactory,
                      final PriorityList<Section> sections,
                      final CandidateHunkSource candidateHunkSource) {
        this.schedulerFactory = schedulerFactory;
        this.sections = sections;
        this.candidateHunkSource = candidateHunkSource;
    }

    /**
     * Runs the scheduling algorithm. When the algorithm completes, returns
     * all the generated hunks as a list.
     * @return a list of generated hunks
     */
    @Override
    public List<Hunk> call() {
        return backtrackingSearch(sections.clone(), schedulerFactory.get());
    }

    private List<Hunk> backtrackingSearch(final PriorityList<Section> sections, final Scheduler scheduler) {
        final Section section = sections.pop();
        if (section == null) {
            return scheduler.getHunks();
        }
        for (final Hunk hunk: candidateHunkSource.iterable(section)) {
            final boolean wasAdded = scheduler.addHunk(hunk);
            if (wasAdded) {
                final List<Hunk> result = backtrackingSearch(sections.clone(), scheduler);
                if (result != null) {
                    return result;
                }
                scheduler.removeHunk(section);
            }
        }
        return null;
    }

}
