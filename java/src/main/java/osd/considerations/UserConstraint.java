package osd.considerations;

import osd.output.Hunk;

/**
 * User constraints use their element pairs as white- or blacklists. See
 * {@linkplain UserConsideration the parent class documentation} for what an
 * "element pair" is. Blacklist constraints are satisfied if at least one
 * member doesn't match. Whitelist constraints are satisfied if either both
 * members match or neither does. Furthermore, all whitelist constraints with
 * the same first member are "OR"ed together, making them suitable for eg.
 * professor qualifications.
 */
public class UserConstraint extends UserConsideration implements Constraint {

    private final boolean blacklist;
    private final Object whitelistKey;

    public UserConstraint(final Object left, final Object right, final boolean isBlacklist) {
        super(left, right);
        this.blacklist = isBlacklist;
        this.whitelistKey = isBlacklist ? null : left;
    }

    @Override
    public boolean test(final Hunk hunk) {
        final Match match = getMatch(hunk);
        if (match == Match.INCONCLUSIVE || match == Match.NEITHER) {
            return true;
        }
        if (blacklist) {
            return match != Match.BOTH;
        } else {
            return match != Match.ONE;
        }
    }

    Object getWhitelistKey() {
        return whitelistKey;
    }

    boolean isBlacklist() {
        return blacklist;
    }

}
