package osd.output;

public interface Callbacks {

    /**
     * Indicate whether we're satisfied with the results we've received.
     * @return whether we're satisfied with the results we've received
     */
    boolean stopCondition();

    /**
     * Called whenever the algorithm generates a complete result.
     * @param results the complete {@link Results result}
     */
    void onCompleteResult(final Results results);

    /**
     * Called whenever the algorithm backtracks. Note that, in the event of a
     * backtrack, the schedule is, by definition, incomplete. Nonetheless,
     * logging such incomplete results may be useful.
     * @param results the incomplete {@link Results results}
     */
    void onBacktrack(final Results results);

}
