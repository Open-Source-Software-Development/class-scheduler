package osd.database;

import osd.considerations.UserConstraint;

class ProfessorQualificationRecord extends Record<UserConstraint> {

    private int id, professorId, courseId;

    @Override
    UserConstraint create(RecordAccession lookup) {
        final Professor professor = lookup.get(Professor.class, professorId);
        final Course course = lookup.get(Course.class, courseId);
        return new UserConstraint(course, professor, false);
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getProfessorId() {
        return professorId;
    }

    public void setProfessorId(final Object professorId) {
        this.professorId = Integer.valueOf(professorId.toString());
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(final Object courseId) {
        this.courseId = Integer.valueOf(courseId.toString());
    }

}
