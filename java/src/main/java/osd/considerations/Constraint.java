package osd.considerations;

import osd.output.Hunk;

/**
 * A predicate over hunks.
 * <p>Since hunks are used as both output and internally during scheduling,
 * it is possible that some members of a hunk may be {@code null}. If a
 * constraint attempts to refer to some member of a hunk for which that member
 * is {@code null}, the constraint msut return {@code true}.</p>
 */
public interface Constraint extends Consideration {

    /**
     * Determines whether some hunk satisfies this consideration. If the
     * consideration is indeterminate because the hunk contains {@code null}s,
     * the result must be {@code true}.
     * @param hunk a hunk
     * @return true if the hunk passes or is indeterminate, false if it fails
     */
    @Override
    boolean test(final Hunk hunk);

}
