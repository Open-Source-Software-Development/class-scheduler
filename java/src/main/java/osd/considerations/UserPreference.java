package osd.considerations;

import osd.database.input.RecordConversion;
import osd.database.input.RecordConverter;
import osd.database.input.record.UserPreferenceRecord;
import osd.schedule.Hunk;

public class UserPreference extends UserConsideration implements Preference {

    private final int worth;

    @RecordConversion
    UserPreference(final UserPreferenceRecord record, final RecordConverter recordConverter) {
        super(record, recordConverter);
        this.worth = record.getScore();
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
    public boolean test(final Hunk hunk) {
        return getMatch(hunk) == Match.BOTH;
    }
}
