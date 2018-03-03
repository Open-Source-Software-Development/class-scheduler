package osd.considerations;

import osd.output.Hunk;

@FunctionalInterface
public interface Preference extends Consideration<Integer> {

    /**
     * Scores a hunk on whether it meets this preference. Typically, this will
     * return either 0 or some constant score, but doesn't have to. Scores
     * may be negative.
     * @param hunk the hunk to evaluate
     * @return a score for the hunk
     */
    @Override
    Integer evaluate(final Hunk hunk);

}
