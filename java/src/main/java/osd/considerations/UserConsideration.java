package osd.considerations;

import osd.input.*;
import osd.output.Hunk;

import java.util.function.Function;

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
public abstract class UserConsideration implements Consideration {

    private final Object left, right;
    private final Function<Hunk, ?> extractLeft, extractRight;

    /**
     * Construct a user consideration with some pair.
     * @param leftClass the type of the first element
     * @param left the first element of the pair
     * @param rightClass the type of the second element
     * @param right the second element of the pair
     * @param <L> leftClass
     * @param <R> rightClass
     */
    <L, R> UserConsideration(final Class<L> leftClass, final L left, final Class<R> rightClass, final R right) {
        this.left = left;
        this.right = right;
        this.extractLeft = getExtractor(leftClass);
        this.extractRight = getExtractor(rightClass);
    }

    enum Match {
        /**
         * Both members are present.
         */
        BOTH,

        /**
         * Only one member is present, but neither are {@code null}.
         */
        ONE,

        /**
         * Neither member is present, but neither are {@code null}.
         */
        NEITHER,

        /**
         * At least one member is {@code null}.
         */
        NULL,
    }

    /**
     * Determines whether both scheduling elements are present in the hunk. If
     * looking up either element returns {@code null}, then the result is
     * {@link Match#NULL}. Otherwise, the result is {@link Match#BOTH}
     * if both members are the ones defined here, {@link Match#ONE}
     * if one is, and {@link Match#NEITHER} if neither are.
     * @param hunk a hunk
     * @return whether both scheduling elements are present
     */
    Match getMatch(final Hunk hunk) {
        final Object leftMatch = extractLeft.apply(hunk);
        if (leftMatch == null) {
            return Match.NULL;
        }
        final Object rightMatch = extractRight.apply(hunk);
        if (rightMatch == null) {
            return Match.NULL;
        }
        final int score = ((leftMatch.equals(left) ? 1 : 0)
                        + (rightMatch.equals(right) ? 1 : 0));
        if (score == 2) {
            return Match.BOTH;
        }
        if (score == 1) {
            return Match.ONE;
        }
        return Match.NEITHER;
    }

    private static Function<Hunk, ?> getExtractor(final Class<?> clazz) {
        if (clazz == Professor.class) {
            return Hunk::getProfessor;
        }
        if (clazz == Room.class) {
            return Hunk::getRoom;
        }
        if (clazz == RoomType.class) {
            return h -> h.getRoom().getRoomType();
        }
        if (clazz == Section.class) {
            return Hunk::getSection;
        }
        if (clazz == Course.class) {
            return h -> h.getSection().getCourse();
        }
        throw new IllegalArgumentException("no extractor defined for " + clazz);
    }

}
