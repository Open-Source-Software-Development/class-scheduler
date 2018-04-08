package osd.database.output;

import javax.persistence.*;

@Entity
@Table(name="scheduler_run")
public class RunRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "sqlite")
    @TableGenerator(name= "sqlite", table = "sqlite_sequence", valueColumnName = "seq", pkColumnName = "name")
    @Column(name="id")
    private int id;

    @Column(name="season_id")
    private int seasonId;

    @Column(name="active")
    private boolean active;

    @Column(name="pid")
    private int pid;

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
    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    @SuppressWarnings("unused")
    public boolean getActive() {
        return active;
    }

    @SuppressWarnings("unused")
    public void setActive(boolean active) {
        this.active = active;
    }

    @SuppressWarnings("unused")
    public int getPid() {
        return pid;
    }

    @SuppressWarnings("unused")
    public void setPid(int pid) {
        this.pid = pid;
    }
}
