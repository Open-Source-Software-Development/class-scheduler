package osd.database;

import osd.considerations.UserConstraint;

import javax.persistence.*;

@Entity
@Table(name="scheduler_qualification")
class QualificationRecord extends Record<UserConstraint> {

    @Id @GeneratedValue
    @Column(name="id")
    private int id;

    @Column(name="professor_id")
    private int professorId;

    @Column(name="course_id")
    private int courseId;

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
