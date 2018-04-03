package osd.database;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

class RoomTypeRecord extends Record<RoomType> {

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

    @Override
    public String getName() {
        return name;
    }

    @Override
    RoomType create(final RecordAccession lookup) {
        return new RoomType(id, name);
    }
}
