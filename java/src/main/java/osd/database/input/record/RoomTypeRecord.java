package osd.database.input.record;

import javax.persistence.*;

@Entity
@Table(name="scheduler_roomtype")
public class RoomTypeRecord implements Record {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    public void setId(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
