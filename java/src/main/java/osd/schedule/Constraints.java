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

class Constraints implements Predicate<Hunk> {

    private final Collection<Constraint> predicates;

    @Inject
    Constraints(final Collection<Constraint> predicates) {
        this.predicates = new ArrayList<>(predicates);
    }

    @Override
    public boolean test(final Hunk hunk) {
        for (final Constraint constraint : predicates) {
            if (!constraint.evaluate(hunk)) {
                return false;
            }
        }
        return true;
    }

    static Predicate<Professor> professorPredicate(final Constraints constraints, final Section section) {
        return p -> constraints.test(new Hunk(section, p, null, null));
    }

    static Predicate<Room> roomPredicate(final Constraints constraints, final Section section) {
        return r -> constraints.test(new Hunk(section, null, r, null));
    }

}
