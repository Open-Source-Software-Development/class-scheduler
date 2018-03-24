package osd.database;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "scheduler_room")
class RoomRecord implements Room {

    // TODO: add column fields, getters, and setters here
    // see https://www.tutorialspoint.com/hibernate/hibernate_annotations.htm

    @Override
    public String getName() {
        // TODO: implement this
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public RoomType getRoomType() {
        // TODO: implement this
        throw new UnsupportedOperationException("not yet implemented");
    }

}
