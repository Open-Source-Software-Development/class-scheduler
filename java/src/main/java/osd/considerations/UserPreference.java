package osd.considerations;

import osd.database.RecordAccession;
import osd.database.input.Block;
import osd.database.input.Professor;
import osd.database.RecordConversion;
import osd.database.input.record.ProfessorConstraintRecord;
import osd.database.input.record.UserPreferenceRecord;
import osd.schedule.Hunk;

import java.util.function.Predicate;

public class UserPreference extends UserConsideration implements Preference {

    // should be negative
    private static int PREFER_AGAINST_WORTH = -1;

    private final int worth;

    @RecordConversion
    UserPreference(final UserPreferenceRecord record, final RecordAccession recordAccession) {
        super(record, recordAccession);
        this.worth = record.getScore();
    }

    @RecordConversion(filter=ProfessorConstraintIsApplicable.class)
    UserPreference(final ProfessorConstraintRecord record, final RecordAccession recordAccession) {
        this(record.getId(),
                recordAccession.get(Professor.class, record.getProfessorId()),
                recordAccession.get(Block.class, record.getBlockId()),
                PREFER_AGAINST_WORTH);
    }

    UserPreference(final int id, final Object left, final Object right, final int worth) {
        super(id, left, right);
        this.worth = worth;
    }

    @Override
    public int worth() {
        return worth;
    }

    @Override
    public int evaluate(final Hunk hunk) {
        return (getMatch(hunk) == Match.BOTH) ? worth : 0;
    }

    private static class ProfessorConstraintIsApplicable implements Predicate<ProfessorConstraintRecord> {

        @SuppressWarnings("unused")
        ProfessorConstraintIsApplicable(final RecordAccession unused) {}

        @Override
        public boolean test(final ProfessorConstraintRecord record) {
            return record.getValue() == 2;
        }
    }

}
