package osd.considerations.base;

import osd.considerations.BaseConstraint;
import osd.considerations.Lookups;
import osd.input.Professor;
import osd.output.Hunk;

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
