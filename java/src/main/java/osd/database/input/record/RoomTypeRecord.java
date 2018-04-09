package osd.database.input.record;

import javax.persistence.*;

@Entity
@Table(name="scheduler_roomtype")
public class RoomTypeRecord {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @SuppressWarnings("unused")
    public void setId(final int id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public int getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setName(final String name) {
        this.name = name;
    }

    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }

}
