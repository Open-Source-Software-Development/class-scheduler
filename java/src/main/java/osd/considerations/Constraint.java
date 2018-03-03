package osd.considerations;

import osd.output.Hunk;

@FunctionalInterface
public interface Constraint extends Consideration<Boolean> {

    /**
     * Checks whether some hunk satisfies this constraint.
     * @param hunk the hunk to evaluate
     * @return whether the hunk satisfies the constraint
     */
    @Override
    Boolean evaluate(final Hunk hunk);

}
