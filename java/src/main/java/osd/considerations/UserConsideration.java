package osd.considerations;

import osd.output.Hunk;

import java.util.function.Function;
import static osd.considerations.HunkExtractor.Extraction;

/**
 * Abstract base class for user preferences and constraints. User preferences
 * and constraints are both defined as pairs of scheduling elements. For
 * example, a constraint indicating Professor Brown can't teach block 7A would
 * have one of its members be Professor Brown and the other be block 7A.
 * <p>What exactly this pair <em>means</em> is up to the specific implementation,
 * however. The purpose of this class is to provide an easy way to check how
 * many members match, not to interpret that result.</p>
 * @see #getMatch(Hunk)
 */
abstract class UserConsideration implements Consideration {

    private final Function<Hunk, Extraction> extractLeft, extractRight;

    /**
     * Construct a user consideration with some pair.
     * @param left the first element of the pair
     * @param right the second element of the pair
     */
    UserConsideration(final Object left, final Object right) {
        this.extractLeft = HunkExtractor.of(left);
        this.extractRight = HunkExtractor.of(right);
    }

    enum Match {

        /**
         * Neither member is present, but neither are {@code null}.
         */
        NEITHER,

        /**
         * Only one member is present, but neither are {@code null}.
         */
        ONE,

        /**
         * Both members are present.
         */
        BOTH,

        /**
         * At least one member is {@code null}.
         */
        INCONCLUSIVE,

    }

    /**
     * Determines whether both scheduling elements are present in the hunk. If
     * looking up either element returns {@code null}, then the result is
     * {@link Match#INCONCLUSIVE}. Otherwise, the result is {@link Match#BOTH}
     * if both members are the ones defined here, {@link Match#ONE}
     * if one is, and {@link Match#NEITHER} if neither are.
     * @param hunk a hunk
     * @return whether both scheduling elements are present
     */
    Match getMatch(final Hunk hunk) {
        final Extraction left = extractLeft.apply(hunk);
        final Extraction right = extractRight.apply(hunk);
        if (left == Extraction.INCONCLUSIVE || right == Extraction.INCONCLUSIVE) {
            return Match.INCONCLUSIVE;
        }
        final int score = ((left == Extraction.MATCH) ? 1 : 0)
                        + ((right == Extraction.MATCH) ? 1 : 0);
        return Match.values()[score];
    }

}
