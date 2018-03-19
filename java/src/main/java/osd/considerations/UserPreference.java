package osd.considerations;

import osd.output.Hunk;

public class UserPreference extends UserConsideration implements Preference {

    private final int worth;

    public <L, R> UserPreference(final Class<L> leftClass, final L left,
                                 final Class<R> rightClass, final R right,
                                 final int worth) {
        super(leftClass, left, rightClass, right);
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
