package schedule;

import osd.considerations.Constraint;
import osd.considerations.Preference;
import osd.input.Section;
import osd.output.Hunk;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.Callable;

public class SchedulingAttempt implements Callable<List<Hunk>> {

    private final PriorityList<Section> sections;
    private final Trackers trackers;
    private final Scheduler scheduler;
    private final Prioritizer prioritizer;

    @Inject
    SchedulingAttempt(final PriorityList<Section> sections, final Trackers trackers, final Constraint constraints,
                      final Preference preferences) {
        this.sections = sections;
        this.trackers = trackers;
        this.scheduler = new Scheduler(trackers, constraints, preferences);
        this.prioritizer = new Prioritizer(trackers);
    }

    @Override
    public List<Hunk> call() {
        return backtrackingSearch(sections);
    }

    private List<Hunk> backtrackingSearch(final PriorityList<Section> sections) {
        final Section section = sections.pop(prioritizer);
        if (section == null) {
            return scheduler.getHunks();
        }
        for (final Hunk hunk: trackers.candidates(section)) {
            final int score = scheduler.schedule(hunk);
            if (score > 0) {
                final List<Hunk> result = backtrackingSearch(sections.clone());
                if (result != null) {
                    return result;
                }
            }
            scheduler.remove(section);
        }
        return null;
    }

}
