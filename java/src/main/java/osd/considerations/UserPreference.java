package osd.considerations;

import osd.output.Hunk;

public class UserPreference extends UserConsideration implements Preference {

    private final int worth;

    public UserPreference(final Object left, final Object right, final int worth) {
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
