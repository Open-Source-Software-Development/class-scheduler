package osd.considerations;

import osd.output.Hunk;

public class UserConstraint extends UserConsideration implements Constraint{

    private final boolean blacklist;

    public <L, R> UserConstraint(final Class<L> leftClass, final L left,
                                 final Class<R> rightClass, final R right,
                                 final boolean blacklist) {
        super(leftClass, left, rightClass, right);
        this.blacklist = blacklist;
    }

    @Override
    public boolean test(final Hunk hunk) {
        return blacklist != super.test(hunk);
    }

}
