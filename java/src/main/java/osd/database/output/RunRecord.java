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

}
