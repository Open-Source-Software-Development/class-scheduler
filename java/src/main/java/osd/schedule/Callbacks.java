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
 * <p>None of these callbacks should have any externally visible side effects.
 * These callbacks are used as {@linkplain java.util.function.Predicate predicates},
 * and any externally visible side-effects would violate the contract of that
 * interface. Instead, the implementation should act as a "stateful filter",
 * building up results internally. A third callback, {@link #done(boolean)}, is
 * provided to define behavior for after the algorithm has completed.</p>
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
     * Called when the algorithm completes. The {@code boolean} argument
     * indicates whether the algorithm completed successfully {@code true} or
     * ran out of candidates {@code false}.
     * @param success whether the algorithm completed successfully
     */
    void done(final boolean success);

}
