package osd.database.input.record;

import javax.persistence.*;

@Entity
@Table(name="scheduler_qualification")
class QualificationRecord {

    @Id @GeneratedValue
    @Column(name="id")
    private int id;

    @Column(name="professor_id")
    private int professorId;

    @Column(name="course_id")
    private int courseId;

    @SuppressWarnings("unused")
    public int getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(final int id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public int getProfessorId() {
        return professorId;
    }

    @SuppressWarnings("unused")
    public void setProfessorId(final Object professorId) {
        this.professorId = Integer.valueOf(professorId.toString());
    }

    @SuppressWarnings("unused")
    public int getCourseId() {
        return courseId;
    }

    @SuppressWarnings("unused")
    public void setCourseId(final Object courseId) {
        this.courseId = Integer.valueOf(courseId.toString());
    }

}
