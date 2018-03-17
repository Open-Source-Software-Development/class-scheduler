package osd.schedule;

import osd.considerations.Constraint;
import osd.input.Room;
import osd.input.Section;
import osd.input.Professor;
import osd.output.Hunk;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

/**
 * A "composite" of all the constraints for a schedule.
 * <p>This class is immutable.</p>
 * @see Constraint
 */
class Constraints implements Predicate<Hunk> {

    private final Collection<Constraint> constraints;

    /**
     * DI constructor.
     * @param constraints the constraints for the schedule
     */
    @Inject
    Constraints(final Collection<Constraint> constraints) {
        this.constraints = new ArrayList<>(constraints);
    }

    /**
     * Checks if a hunk meets <em>all</em> constraints.
     * @param hunk the hunk
     * @return whether it meets all constraints
     */
    @Override
    public boolean test(final Hunk hunk) {
        for (final Constraint constraint : constraints) {
            if (!constraint.evaluate(hunk)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Converts a {@code Constraints} object into a predicate for professors.
     * The resulting predicate's {@link Predicate#test(Object)} method wraps the
     * supplied section and the professor to test in a hunk, and passes that
     * hunk back to the {@code Constraints} instance.
     * @param constraints the {@code Constraints} instance
     * @param section the section to test against
     * @return a predicate testing professors against that section
     */
    static Predicate<Professor> professorPredicate(final Constraints constraints, final Section section) {
        return p -> constraints.test(new Hunk(section, p, null, null));
    }

    /**
     * Converts a {@code Constraints} object into a predicate for rooms.
     * The resulting predicate's {@link Predicate#test(Object)} method wraps the
     * supplied section and the room to test in a hunk, and passes that
     * hunk back to the {@code Constraints} instance.
     * @param constraints the {@code Constraints} instance
     * @param section the section to test against
     * @return a predicate testing rooms against that section
     */
    static Predicate<Room> roomPredicate(final Constraints constraints, final Section section) {
        return r -> constraints.test(new Hunk(section, null, r, null));
    }

}
