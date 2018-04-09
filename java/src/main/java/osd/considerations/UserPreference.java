package osd.considerations;

import osd.database.input.Block;
import osd.database.input.Professor;
import osd.database.RecordConversion;
import osd.database.RecordConverter;
import osd.database.input.record.ProfessorConstraintRecord;
import osd.database.input.record.UserPreferenceRecord;
import osd.schedule.Hunk;

public class UserPreference extends UserConsideration implements Preference {

    // should be negative
    private static int PREFER_AGAINST_WORTH = -1;

    private final int worth;

    @RecordConversion
    UserPreference(final UserPreferenceRecord record, final RecordConverter recordConverter) {
        super(record, recordConverter);
        this.worth = record.getScore();
    }

    @RecordConversion(filter="professorConstraintIsApplicable")
    UserPreference(final ProfessorConstraintRecord record, final RecordConverter recordConverter) {
        this(recordConverter.get(Professor.class, record.getProfessorId()),
                recordConverter.get(Block.class, record.getBlockId()),
                PREFER_AGAINST_WORTH);
    }

    UserPreference(final Object left, final Object right, final int worth) {
        super(left, right);
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

    @SuppressWarnings("unused")
    private static boolean professorConstraintIsApplicable(final ProfessorConstraintRecord record) {
        return record.getValue() == 2;
    }

}
