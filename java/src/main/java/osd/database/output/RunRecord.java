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

    @Column(name="season")
    private int seasonId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }
}
