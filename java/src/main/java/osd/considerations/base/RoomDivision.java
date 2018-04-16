package osd.considerations.base;

import osd.considerations.BaseConstraint;
import osd.considerations.Constraint;
import osd.schedule.Lookups;
import osd.database.input.*;
import osd.schedule.Hunk;

import java.util.function.Predicate;

class RoomDivision implements BaseConstraint {

    @Override
    public Constraint bind(final Lookups lookups) {
        return h -> {
            final Room room = h.getRoom();
            final String subject = room.getSubject();
            final Course course = h.getSection().getCourse();
            final String program = course.getProgram();
            return subject.equals(program);
        };
    }

}
