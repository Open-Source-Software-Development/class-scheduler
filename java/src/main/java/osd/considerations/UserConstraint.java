package osd.considerations;

import osd.output.Hunk;

/**
 * User constraints use their element pairs as white- or blacklists. See
 * {@linkplain UserConsideration the parent class documentation} for what an
 * "element pair" is. Blacklist constraints are satisfied if at least one
 * member doesn't match. Whitelist constraints are satisfied if either both
 * members match or neither does.
 */
public class UserConstraint extends UserConsideration implements Constraint {

    private final boolean blacklist;

    public <L, R> UserConstraint(final Class<L> leftClass, final L left,
                                 final Class<R> rightClass, final R right,
                                 final boolean blacklist) {
        super(leftClass, left, rightClass, right);
        this.blacklist = blacklist;
    }

    @Override
    public boolean test(final Hunk hunk) {
        final Match match = getMatch(hunk);
        if (match == Match.NULL) {
            return true;
        }
        if (blacklist) {
            return match != Match.BOTH;
        } else {
            return match != Match.ONE;
        }
    }

}
