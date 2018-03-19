package osd.schedule;

import osd.output.Callbacks;

/**
 * Schedule algorithm entry point. Runs the algorithm itself, accepting
 * {@linkplain Callbacks callbacks} to handle successful and failed results.
 */
public interface Scheduler {

    /**
     * Runs the scheduler algorithm. The {@link Callbacks} argument customizes
     * the scheduling behavior by providing a <em>stop condition</em> (eg. "run
     * until 5 schedules have been generated") and output callbacks. The return
     * value indicates whether the stop condition was actually met, or whether
     * we ran out of candidates first.
     * @param callbacks callbacks for handling results
     * @return whether we met the stop condition before running out of candidates
     */
    boolean run(final Callbacks callbacks);

}
