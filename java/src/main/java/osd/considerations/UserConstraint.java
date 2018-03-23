package osd.considerations;

import osd.output.Hunk;
import osd.util.ImmutablePair;
import osd.util.Pair;

/**
 * User constraints whitelist or blacklist pairs of scheduling elements. For
 * blacklist constraints, any hunk containing both elements of the pair
 * violates the constraint. Whitelist constraints are more complicated.
 * <p>The behavior of whitelist constraints partially depends on the
 * <em>types</em> of elements being evaluated, not just their values.
 * The first element of a whitelist constraint's pair is taken as a "whitelist
 * key", and all user constraints with the same whitelist key are grouped
 * together. Within those groupings, they are further grouped by the type of
 * their second element, using the same algorithm as {@link HunkField#get(Object)}.
 * Groups of whitelist constraints are only considered if the hunk contains the
 * group's whitelist key. Within the grouping, at least one member of each
 * subgroup must pass.</p>
 * <p>As an extended example, suppose that CSI-666 must be taught by Professor
 * Mephistopheles, Beelzebub, or Cthulhu. It must also be taught in either Hell
 * or a Mac lab (zing!). This would give us five whitelist user constraints.
 * Since they all have CSI-666 as their first element, they're grouped by that.
 * However, the first three have second elements of type Professor, while the
 * latter two are of type Room. Therefore, we have two subgroups, and any hunk
 * for a section of that class must meet one constraint from both subgroups. Of
 * course, a hunk for a section of another class would automatically pass these
 * constraints, since it's for a different class.</p>
 */
public class UserConstraint extends UserConsideration implements Constraint {

    private final Pair<Object, Object> debug;
    private final Pair<Object, HunkField<?>> whitelistKey;

    public UserConstraint(final Object left, final Object right, final boolean isBlacklist) {
        super(left, right);
        if (isBlacklist) {
            whitelistKey = ImmutablePair.of(null, null);
        } else {
            whitelistKey = ImmutablePair.of(left, HunkField.get(right));
        }
        debug = ImmutablePair.of(left, right);
    }

    @Override
    public boolean test(final Hunk hunk) {
        final Match match = getMatch(hunk);
        if (match == Match.INCONCLUSIVE || match == Match.NEITHER) {
            return true;
        }
        // Blacklists reject any hunk containing both
        // of their elements. Whitelists reject hunks
        // containing only one, but not both.
        if (whitelistKey.isEmpty()) {
            return match != Match.BOTH;
        } else {
            return match != Match.LEFT;
        }
    }

    @Override
    public String toString() {
        return (whitelistKey.isEmpty() ? "Blacklist" : "Whitelist") + debug;
    }

    // TODO: put this back to package-visible when placeholders are removed
    public Pair<Object, HunkField<?>> getWhitelistKey() {
        return whitelistKey;
    }

}
