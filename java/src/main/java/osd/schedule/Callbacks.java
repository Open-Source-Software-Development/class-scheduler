package osd.schedule;

/**
 * Callbacks to accept generated schedules. There are two main callbacks:
 * <ol>
 *     <li>{@link #onCompleteResult(Results)} for complete results, and</li>
 *     <li>{@link #onBacktrack(Results)} for when the algorithm backtracks.</li>
 * </ol>
 * Either callback can act as a <em>stop condition</em>. Both have a
 * {@code boolean] return value that indicates whether the algorithm should
 * stop. Once a stop condition is met, no further candidates will be generated,
 * and no further callbacks will occur.
 * <p>If the algorithm runs out of candidates before a stop condition is met,
 * {@link #onSchedulingFailed()} will be called to indicate that.</p>
 */
public interface Callbacks {


    /**
     * Called whenever the algorithm generates a complete result. The return
     * value indicates if we're "satisfied" with this result. If it's true,
     * the algorithm will stop. Otherwise, it will continue.
     * @param results the complete {@link Results result}
     * @return true if the algorithm should stop, false if it should continue
     */
    boolean onCompleteResult(final Results results);

    /**
     * Called whenever the algorithm backtracks. Note that, in the event of a
     * backtrack, the schedule is, by definition, incomplete. Nonetheless,
     * logging such incomplete results may be useful.
     * <p>As with {@link #onCompleteResult(Results)} (Results)}, the return value
     * indicates whether the algorithm should stop. While stopping on an
     * incomplete result may seem counter-intuitive, it may be desirable if the
     * result is close to complete and scores well.</p>
     * @param results the incomplete {@link Results results}
     * @return true if the algorithm should stop, false if it should continue
     */
    boolean onBacktrack(final Results results);

    /**
     * Called if the algorithm runs out of candidates before the stop condition.
     */
    void onSchedulingFailed();

}
