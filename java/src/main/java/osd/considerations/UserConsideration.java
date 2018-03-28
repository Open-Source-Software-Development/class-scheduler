package osd.considerations;

import osd.schedule.Hunk;

import java.util.function.Function;
import static osd.considerations.HunkField.Extraction;

/**
 * Abstract base class for user preferences and constraints. User preferences
 * and constraints are both represented in the database as pairs of scheduling
 * elements. This class exists as a convenience to help read them in.
 * Interpretation of those element pairs is up to the specific implementation.
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
        this.extractLeft = HunkField.get(left).getExtractor(left);
        this.extractRight = HunkField.get(right).getExtractor(right);
    }

    enum Match {

        /**
         * Neither member is present, but neither are {@code null}.
         */
        NEITHER,

        /**
         * Only the left-hand member is present, but neither are {@code null}.
         */
        LEFT,

        /**
         * Only the right-hand member is present, but neither are {@code null}.
         */
        RIGHT,

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
     * Compare a hunk to this consideration's element pair. If the hunk is
     * incomplete and the presence of either element is inconclusive, return
     * {@link Match#INCONCLUSIVE}. Otherwise, return {@link Match#BOTH} if both
     * elements of this consideration's element pair are present,
     * {@link Match#LEFT} or {@link Match#RIGHT}if only one is, and
     * {@link Match#NEITHER} if neither are.
     * @see HunkField for notes on what it means for a hunk to contain something
     * @param hunk a hunk
     * @return whether both scheduling elements are present
     */
    Match getMatch(final Hunk hunk) {
        final Extraction left = extractLeft.apply(hunk);
        final Extraction right = extractRight.apply(hunk);
        if (left == Extraction.INCONCLUSIVE || right == Extraction.INCONCLUSIVE) {
            return Match.INCONCLUSIVE;
        }
        if (left == Extraction.MATCH && right == Extraction.MATCH) {
            return Match.BOTH;
        }
        if (left == Extraction.NO_MATCH && right == Extraction.NO_MATCH) {
            return Match.NEITHER;
        }
        return (left == Extraction.MATCH) ? Match.LEFT : Match.RIGHT;
    }

}
