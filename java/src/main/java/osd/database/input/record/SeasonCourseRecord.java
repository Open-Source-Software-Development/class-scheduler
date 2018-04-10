package osd.database.input.record;

import javax.persistence.*;

@Entity
@Table(name = "scheduler_season_courses")
public class SeasonCourseRecord {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name="season_id")
    private int seasonId;

    @Column(name="course_id")
    private int courseId;

    @SuppressWarnings("unused")
    public int getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(int id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public int getSeasonId() {
        return seasonId;
    }

    @SuppressWarnings("unused")
    public void setSeasonId(Object seasonId) {
        this.seasonId = Integer.valueOf(seasonId.toString());
    }

    @SuppressWarnings("unused")
    public int getCourseId() {
        return courseId;
    }

    @SuppressWarnings("unused")
    public void setCourseId(Object courseId) {
        this.courseId = Integer.valueOf(courseId.toString());
    }
}
