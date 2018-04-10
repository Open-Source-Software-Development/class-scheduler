package osd.considerations;

import osd.database.Identified;
import osd.database.RecordAccession;
import osd.database.input.record.UserConsiderationRecord;
import osd.schedule.Hunk;

import java.util.function.Function;

/**
 * Abstract base class for user preferences and constraints. User preferences
 * and constraints are both represented in the database as pairs of scheduling
 * elements. This class exists as a convenience to help read them in.
 * Interpretation of those element pairs is up to the specific implementation.
 * @see #getMatch(Hunk)
 */
class UserConsideration implements Identified {

    private final int id;
    final Object left, right;
    private final Function<Hunk, HunkField.Extraction> extractLeft, extractRight;

    UserConsideration(final UserConsiderationRecord record, final RecordAccession recordAccession) {
        this(record.getId(),
                recordAccession.getGeneric(record.getLeftTypeId(), record.getLeftId()),
                recordAccession.getGeneric(record.getRightTypeId(), record.getRightId()));
    }

    UserConsideration(final int id, final Object left, final Object right) {
        this.id = id;
        this.left = left;
        this.right = right;
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
        final HunkField.Extraction left = extractLeft.apply(hunk);
        final HunkField.Extraction right = extractRight.apply(hunk);
        if (left == HunkField.Extraction.INCONCLUSIVE || right == HunkField.Extraction.INCONCLUSIVE) {
            return Match.INCONCLUSIVE;
        }
        if (left == HunkField.Extraction.MATCH && right == HunkField.Extraction.MATCH) {
            return Match.BOTH;
        }
        if (left == HunkField.Extraction.NO_MATCH && right == HunkField.Extraction.NO_MATCH) {
            return Match.NEITHER;
        }
        return (left == HunkField.Extraction.MATCH) ? Match.LEFT : Match.RIGHT;
    }

    @Override
    public int getId() {
        return id;
    }

}
