package osd.considerations;

import osd.output.Hunk;

/**
 * A predicate over hunks.
 * <p>Since hunks are used as both output and internally during scheduling,
 * it is possible that some members of a hunk may be {@code null}. If a
 * constraint attempts to refer to some member of a hunk for which that member
 * is {@code null}, the constraint must return {@code true}.</p>
 */
public interface Constraint extends Consideration {

    /**
     * Determines whether some hunk satisfies this consideration. If the
     * consideration is indeterminate because the hunk contains {@code null}s,
     * the result must be {@code true}.
     * @param hunk a hunk
     * @return true if the hunk passes or is indeterminate, false if it fails
     */
    @Override
    boolean test(final Hunk hunk);

    /**
     * Returns the logical "AND" of two constraints, as a constraint.
     * @param other the other constraint
     * @return the "AND" of both constraints
     */
    default Constraint and(final Constraint other) {
        final Constraint that = this;
        return new Constraint() {

            @Override
            public boolean test(final Hunk hunk) {
                return that.test(hunk) && other.test(hunk);
            }

            @Override
            public String toString() {
                return that + " && " + other;
            }
        };
    }

    /**
     * Returns the logical "OR" of two constraints, as a constraint.
     * @param other the other constraint
     * @return the "OR" of both constraints
     */
    default Constraint or(final Constraint other) {
        final Constraint that = this;
        return new Constraint() {

            @Override
            public boolean test(final Hunk hunk) {
                return that.test(hunk) || other.test(hunk);
            }

            @Override
            public String toString() {
                return that + " || " + other;
            }
        };
    }

    /**
     * A constraint that always returns {@code true}. Its {@link #and(Constraint)}
     * and {@link #or(Constraint)} operations are optimized to just return the
     * other constraint, since that's logically equivalent.
     */
    Constraint DUMMY = new Constraint() {

        @Override
        public boolean test(Hunk hunk) {
            return true;
        }

        @Override
        public Constraint and(final Constraint other) {
            return other;
        }

        @Override
        public Constraint or(final Constraint other) {
            return other;
        }

    };

}
