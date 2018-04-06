package osd.database.input.record;

import javax.persistence.*;

@Entity
@Table(name="scheduler_qualification")
class QualificationRecord implements Record {

    @Id @GeneratedValue
    @Column(name="id")
    private int id;

    @Column(name="professor_id")
    private int professorId;

    @Column(name="course_id")
    private int courseId;

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
