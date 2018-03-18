package osd.schedule;

import osd.output.Hunk;

import java.util.List;

/**
 * An attempt to create a schedule.
 */
public interface Scheduler {

    /**
     * Runs the scheduling algorithm. The result includes all the hunks that
     * were generated. If generation failed, then the result is {@code null}
     * instead. Except this to change to something more useful very soon.
     * @return generated hunks or {@code null}
     */
    List<Hunk> getResult();

}
