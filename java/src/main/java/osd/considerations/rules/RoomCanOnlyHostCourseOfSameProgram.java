package osd.considerations.rules;

import osd.considerations.BaseConstraint;
import osd.considerations.Constraint;
import osd.schedule.Lookups;
import osd.database.input.*;

class RoomCanOnlyHostCourseOfSameProgram implements BaseConstraint {

    // TODO: this should really be applied "early", as user constraints are

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
