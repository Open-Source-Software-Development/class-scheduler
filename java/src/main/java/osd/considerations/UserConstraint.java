package osd.considerations;

import osd.database.RecordConversion;
import osd.database.RecordAccession;
import osd.database.input.*;
import osd.database.input.record.ProfessorConstraintRecord;
import osd.database.input.record.QualificationRecord;
import osd.database.input.record.UserConstraintRecord;
import osd.schedule.Hunk;
import osd.util.pair.ImmutablePair;
import osd.util.pair.Pair;

import java.util.function.Predicate;

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
 * course, a hunk for a section of another course would automatically pass these
 * constraints, since it's for a different course with its own constraints.</p>
 */
public class UserConstraint extends UserConsideration implements Constraint {

    private final Pair<Object, HunkField<?>> whitelistKey;

    @RecordConversion
    UserConstraint(final UserConstraintRecord record, final RecordAccession recordAccession) {
        super(record, recordAccession);
        if (record.getIsBlacklist()) {
            whitelistKey = ImmutablePair.of(null, null);
        } else {
            whitelistKey = ImmutablePair.of(left, HunkField.get(right));
        }
    }

    @RecordConversion
    UserConstraint(final QualificationRecord record, final RecordAccession recordAccession) {
        this(record.getId(),
                recordAccession.get(Course.class, record.getCourseId()),
                recordAccession.get(Professor.class, record.getProfessorId()),
                false);

    }

    @RecordConversion(filter=ProfessorConstraintIsApplicable.class)
    UserConstraint(final ProfessorConstraintRecord record, final RecordAccession recordAccession) {
        this(record.getId(),
                recordAccession.get(Professor.class, record.getProfessorId()),
                recordAccession.get(Block.class, record.getBlockId()),
                true);
    }

    UserConstraint(final int id, final Object left, final Object right, final boolean isBlacklist) {
        super(id, left, right);
        if (isBlacklist) {
            whitelistKey = ImmutablePair.of(null, null);
        } else {
            whitelistKey = ImmutablePair.of(left, HunkField.get(right));
        }
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

    Pair<Object, HunkField<?>> getWhitelistKey() {
        return whitelistKey;
    }

    private static class ProfessorConstraintIsApplicable implements Predicate<ProfessorConstraintRecord> {

        @SuppressWarnings("unused")
        ProfessorConstraintIsApplicable(final RecordAccession unused) {}

        @Override
        public boolean test(final ProfessorConstraintRecord record) {
            return record.getValue() == 1;
        }
    }

}
