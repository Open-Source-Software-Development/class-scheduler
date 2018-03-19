package osd.considerations.base;

import osd.considerations.BaseConstraint;
import osd.considerations.Constraint;
import osd.considerations.Lookups;
import osd.input.Professor;

class ProfessorCantTeachTooManyCourses implements BaseConstraint {

    @Override
    public Constraint bind(final Lookups lookups) {
        return h -> {
            final Professor professor = h.getProfessor();
            final long count = lookups.lookup(professor).count();
            return count < professor.getCourseCapacity();
        };
    }

}
