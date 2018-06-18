package osd.database.output;

import osd.database.Identified;

import javax.persistence.*;

@Entity
@Table(name="scheduler_season")
public class SeasonRecord implements Identified {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "sqlite")
    @TableGenerator(name= "sqlite", table = "sqlite_sequence", valueColumnName = "seq", pkColumnName = "name")
    @Column(name="id")
    private int id;

    @SuppressWarnings("unused")
    public int getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(int id) {
        this.id = id;
    }

}
