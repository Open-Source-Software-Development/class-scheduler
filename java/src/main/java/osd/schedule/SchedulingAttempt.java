package osd.schedule;

import osd.output.Hunk;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * An attempt to schedule some classes. Once one is constructed, its
 * {@link #call()} method may be invoked to attempt hunk generation.
 */
public class SchedulingAttempt implements Callable<List<Hunk>> {

    private final WIPSchedule wipSchedule;

    @Inject
    SchedulingAttempt(final WIPSchedule wipSchedule) {
        this.wipSchedule = wipSchedule;
    }

    /**
     * Runs the scheduling algorithm. When the algorithm completes, returns
     * all the generated hunks as a list.
     * @return a list of generated hunks
     */
    @Override
    public List<Hunk> call() {
        return wipSchedule.getResult();
    }

}
