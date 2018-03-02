package osd.schedule;

import osd.input.Section;
import osd.output.Hunk;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.Callable;

public class SchedulingAttempt implements Callable<List<Hunk>> {

    private final PriorityList<Section> sections;
    private final CandidateHunkSource candidateHunkSource;
    private final Scheduler scheduler;

    @Inject
    SchedulingAttempt(final Scheduler scheduler, final PriorityList<Section> sections,
                      final CandidateHunkSource candidateHunkSource) {
        this.scheduler = scheduler;
        this.sections = sections;
        this.candidateHunkSource = candidateHunkSource;
    }

    @Override
    public List<Hunk> call() {
        return backtrackingSearch(sections);
    }

    private List<Hunk> backtrackingSearch(final PriorityList<Section> sections) {
        final Section section = sections.pop();
        if (section == null) {
            return scheduler.getHunks();
        }
        for (final Hunk hunk: candidateHunkSource.iterable(section)) {
            final boolean wasAdded = scheduler.addHunk(hunk);
            if (wasAdded) {
                final List<Hunk> result = backtrackingSearch(sections.clone());
                if (result != null) {
                    return result;
                }
                scheduler.removeHunk(section);
            }
        }
        return null;
    }

}
