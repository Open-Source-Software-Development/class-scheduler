package osd.considerations;

import osd.schedule.Hunk;

import java.util.Objects;
import java.util.function.Predicate;

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
public interface Preference {

    /**
     * Awards (or deducts) points for some hunk. This should return either
     * zero or any integer not greater than the value returned by {@link #worth()}.
     * The value may be negative.
     * @param hunk the hunk to score
     * @return this preference's score for that hunk
     */
    int evaluate(Hunk hunk);

    /**
     * The upper limit of the score this preference may award. However,
     * preferences with negative "worth" values may still award zero points.
     * This value must be constant.
     * @return the upper limit of the score this preference awards
     */
    int worth();

    /**
     * Returns a preference governed by some predicate. If the predicate's
     * {@link Predicate#test(Object)} method returns {@code true}, the hunk's
     * score is adjusted by the {@code worth} parameter. Otherwise, the score
     * is left unmodified. As always, the worth may be negative.
     * @param predicate the predicate that defines the preference
     * @param worth how much the preference is worth
     * @return a preference defined by the above
     */
    static Preference of(final Predicate<Hunk> predicate, final int worth) {
        Objects.requireNonNull(predicate);
        return new Preference() {

            @Override
            public int evaluate(final Hunk hunk) {
                final boolean result = predicate.test(hunk);
                return result ? worth : 0;
            }

            @Override
            public int worth() {
                return worth;
            }

        };
    }

}
