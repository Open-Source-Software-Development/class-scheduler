package osd.database.output;

import javax.persistence.*;

@Entity
@Table(name="scheduler_section")
public class SectionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "sqlite")
    @TableGenerator(name= "sqlite", table = "sqlite_sequence", valueColumnName = "seq", pkColumnName = "name")
    @Column(name="id")
    private int id;

    @Column(name="run_id")
    private int runId;

    @Column(name="course_id")
    private int courseId;

    @Column(name="section_identifier")
    private String suffix;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public int getRunId() {
        return runId;
    }

    public void setRunId(int runId) {
        this.runId = runId;
    }
}
