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

}
