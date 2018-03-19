package osd.schedule;

import osd.output.Callbacks;

/**
 * An attempt to create a schedule.
 */
public interface Scheduler {

    boolean run(final Callbacks callbacks);

}
