package osd.considerations;

import osd.schedule.Hunk;

/**
 * A guideline as to what hunks are preferable. During candidate hunk
 * generation, all preferences have a chance to "score" the hunk. The
 * scores awarded by all preferences are summed, and hunks that score higher
 * are considered first.
 * <p>Each preference is {@link #worth()} something. This is a guideline to how
 * high its score might be, but the preference may award other scores. In
 * particular, a preference is always free to award a lower score, and it may
 * always award a score of zero. The explicit "worth" value makes it possible
 * to determine the maximum score a hunk can have.</p>
 * <p>Preferences may award negative scores.</p>
 */
interface Preference extends Consideration {

    /**
     * Awards (or deducts) points for some hunk. This should return either
     * zero or any integer not greater than the value returned by {@link #worth()}.
     * The value may be negative.
     * <p>The default implementation calls {@link #test(Object)} and returns
     * {@link #worth()} if the hunk passes, zero otherwise.</p>
     * @param hunk the hunk to score
     * @return this preference's score for that hunk
     */
    default int evaluate(final Hunk hunk) {
        return test(hunk) ? worth() : 0;
    }

    /**
     * The upper limit of the score this preference may award. However,
     * preferences with negative "worth" values may still award zero points.
     * This value must be constant.
     * @return the upper limit of the score this preference awards
     */
    int worth();

}
