package osd.considerations.base;

import osd.considerations.BaseConstraint;
import osd.schedule.Lookups;
import osd.database.input.Professor;
import osd.schedule.Hunk;

import java.util.function.Predicate;

class ProfessorCantTeachTooManyCourses implements BaseConstraint {

    @Override
    public Predicate<Hunk> bindPredicate(final Lookups lookups) {
        return h -> {
            final Professor professor = h.getProfessor();
            final long count = lookups.lookup(professor).count();
            return count < professor.getCourseCapacity();
        };
    }

}
